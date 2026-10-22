package com.energy.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.energy.platform.dto.PredictionRequest;
import com.energy.platform.entity.PowerReading;
import com.energy.platform.entity.PredictionResult;
import com.energy.platform.mapper.PowerReadingMapper;
import com.energy.platform.service.PredictionEngineService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TransformerPredictionEngineService implements PredictionEngineService {
    private static final int LOOKBACK = 96;
    private static final int FEATURE_SIZE = 8;
    private static final int D_MODEL = 16;
    private static final int HEADS = 2;
    private static final int HEAD_DIM = D_MODEL / HEADS;

    private final PowerReadingMapper powerReadingMapper;
    private final TinyTransformerModel model = new TinyTransformerModel();
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TransformerPredictionEngineService(PowerReadingMapper powerReadingMapper) {
        this.powerReadingMapper = powerReadingMapper;
    }

    @Override
    public PredictionResult predict(PredictionRequest request, Long taskId) {
        List<PowerReading> rows = powerReadingMapper.selectList(new LambdaQueryWrapper<PowerReading>()
            .eq(PowerReading::getStationId, request.getStationId())
            .orderByDesc(PowerReading::getRecordTime)
            .last("LIMIT " + LOOKBACK));
        rows = new ArrayList<>(rows);
        rows.sort(Comparator.comparing(PowerReading::getRecordTime));

        int steps = Math.max(1, request.getHorizon() == null ? 8 : request.getHorizon() * 4);
        List<Double> predictions = model.forecast(rows, steps);
        LocalDateTime predictionTime = LocalDateTime.now();
        String resultData = toResultJson(predictions, predictionTime);

        PredictionResult result = new PredictionResult();
        result.setStationId(request.getStationId());
        result.setTaskId(taskId);
        result.setModelType("Attention-Heuristic");
        result.setPredictionTime(predictionTime);
        result.setHorizon(request.getHorizon());
        result.setMetrics("{\"evaluation\":\"pending-ground-truth\",\"model\":\"attention-heuristic\"}");
        result.setResultData(resultData);
        return result;
    }

    private String toResultJson(List<Double> predictions, LocalDateTime predictionTime) {
        StringBuilder data = new StringBuilder("[");
        for (int i = 0; i < predictions.size(); i++) {
            if (i > 0) {
                data.append(',');
            }
            data.append("{\"time\":\"")
                .append(predictionTime.plusMinutes((long) (i + 1) * 15).format(TIME_FORMATTER))
                .append("\",\"power\":")
                .append(Math.max(0, Math.round(predictions.get(i))))
                .append("}");
        }
        return data.append(']').toString();
    }

    private static double number(BigDecimal value) {
        return value == null ? 0.0 : value.doubleValue();
    }

    private static final class TinyTransformerModel {
        List<Double> forecast(List<PowerReading> rows, int steps) {
            if (rows.isEmpty()) {
                return fallback(steps, 1000.0);
            }

            double[][] features = buildFeatures(rows);
            double[] mean = mean(features);
            double[] std = std(features, mean);
            double[][] normalized = normalize(features, mean, std);
            double[] context = transformerContext(normalized);

            List<Double> output = new ArrayList<>();
            PowerReading last = rows.get(rows.size() - 1);
            double lastPower = number(last.getPowerKw());
            double avgPower = rows.stream().map(PowerReading::getPowerKw).mapToDouble(TransformerPredictionEngineService::number).average().orElse(lastPower);
            double trend = trend(rows);

            for (int step = 1; step <= steps; step++) {
                double futureWind = Math.max(0, number(last.getWindSpeed()) + 0.12 * step + context[2] * 0.08);
                double futureIrradiance = Math.max(0, number(last.getIrradiance()) + context[3] * 18.0);
                double attentionSignal = context[0] * std[0] + mean[0];
                double weatherSignal = windCurve(futureWind, Math.max(avgPower * 1.8, 1200)) + futureIrradiance * 0.18;
                double seasonal = Math.sin(step / 3.0) * Math.max(20.0, avgPower * 0.025);
                double predicted = 0.52 * attentionSignal + 0.25 * lastPower + 0.18 * weatherSignal + trend * step + seasonal;
                predicted = Math.max(0, predicted);
                output.add(predicted);
                lastPower = predicted;
            }
            return output;
        }

        private double[][] buildFeatures(List<PowerReading> rows) {
            double[][] x = new double[rows.size()][FEATURE_SIZE];
            for (int i = 0; i < rows.size(); i++) {
                PowerReading r = rows.get(i);
                int minuteOfDay = r.getRecordTime() == null ? 0 : r.getRecordTime().getHour() * 60 + r.getRecordTime().getMinute();
                double angle = 2.0 * Math.PI * minuteOfDay / 1440.0;
                x[i][0] = number(r.getPowerKw());
                x[i][1] = number(r.getWindSpeed());
                x[i][2] = number(r.getIrradiance());
                x[i][3] = number(r.getTemperature());
                x[i][4] = number(r.getHumidity());
                x[i][5] = Math.sin(angle);
                x[i][6] = Math.cos(angle);
                x[i][7] = "GOOD".equalsIgnoreCase(r.getDataQuality()) ? 1.0 : 0.0;
            }
            return x;
        }

        private double[] transformerContext(double[][] x) {
            double[][] embedded = embed(x);
            addPositionalEncoding(embedded);
            double[][] attended = selfAttention(embedded);
            double[][] encoded = feedForward(attended);
            double[] context = new double[D_MODEL];
            for (int t = 0; t < encoded.length; t++) {
                double weight = Math.exp((double) t / Math.max(1, encoded.length - 1));
                for (int d = 0; d < D_MODEL; d++) {
                    context[d] += encoded[t][d] * weight;
                }
            }
            double divisor = 0;
            for (int t = 0; t < encoded.length; t++) {
                divisor += Math.exp((double) t / Math.max(1, encoded.length - 1));
            }
            for (int d = 0; d < D_MODEL; d++) {
                context[d] /= divisor;
            }
            return context;
        }

        private double[][] embed(double[][] x) {
            double[][] out = new double[x.length][D_MODEL];
            for (int t = 0; t < x.length; t++) {
                for (int d = 0; d < D_MODEL; d++) {
                    double sum = 0;
                    for (int f = 0; f < FEATURE_SIZE; f++) {
                        sum += x[t][f] * weight(f, d, 0.37);
                    }
                    out[t][d] = sum / Math.sqrt(FEATURE_SIZE);
                }
            }
            return out;
        }

        private void addPositionalEncoding(double[][] x) {
            for (int t = 0; t < x.length; t++) {
                for (int d = 0; d < D_MODEL; d++) {
                    double div = Math.pow(10000.0, (2.0 * (d / 2)) / D_MODEL);
                    x[t][d] += d % 2 == 0 ? Math.sin(t / div) : Math.cos(t / div);
                }
            }
        }

        private double[][] selfAttention(double[][] x) {
            double[][] out = new double[x.length][D_MODEL];
            for (int h = 0; h < HEADS; h++) {
                int offset = h * HEAD_DIM;
                for (int t = 0; t < x.length; t++) {
                    double[] scores = new double[x.length];
                    for (int j = 0; j < x.length; j++) {
                        double dot = 0;
                        for (int d = 0; d < HEAD_DIM; d++) {
                            double q = x[t][offset + d] * weight(d, h + 1, 0.19);
                            double k = x[j][offset + d] * weight(d, h + 2, 0.23);
                            dot += q * k;
                        }
                        scores[j] = dot / Math.sqrt(HEAD_DIM);
                    }
                    double[] attention = softmax(scores);
                    for (int j = 0; j < x.length; j++) {
                        for (int d = 0; d < HEAD_DIM; d++) {
                            double v = x[j][offset + d] * weight(d, h + 3, 0.29);
                            out[t][offset + d] += attention[j] * v;
                        }
                    }
                }
            }
            return out;
        }

        private double[][] feedForward(double[][] x) {
            double[][] out = new double[x.length][D_MODEL];
            for (int t = 0; t < x.length; t++) {
                for (int d = 0; d < D_MODEL; d++) {
                    double hidden = Math.tanh(x[t][d] * 0.82 + weight(t + 1, d + 1, 0.11));
                    out[t][d] = 0.7 * x[t][d] + 0.3 * hidden;
                }
            }
            return out;
        }

        private double[] softmax(double[] scores) {
            double max = scores[0];
            for (double score : scores) {
                max = Math.max(max, score);
            }
            double sum = 0;
            double[] out = new double[scores.length];
            for (int i = 0; i < scores.length; i++) {
                out[i] = Math.exp(scores[i] - max);
                sum += out[i];
            }
            for (int i = 0; i < out.length; i++) {
                out[i] /= sum;
            }
            return out;
        }

        private double[] mean(double[][] x) {
            double[] mean = new double[FEATURE_SIZE];
            for (double[] row : x) {
                for (int f = 0; f < FEATURE_SIZE; f++) {
                    mean[f] += row[f];
                }
            }
            for (int f = 0; f < FEATURE_SIZE; f++) {
                mean[f] /= x.length;
            }
            return mean;
        }

        private double[] std(double[][] x, double[] mean) {
            double[] std = new double[FEATURE_SIZE];
            for (double[] row : x) {
                for (int f = 0; f < FEATURE_SIZE; f++) {
                    double diff = row[f] - mean[f];
                    std[f] += diff * diff;
                }
            }
            for (int f = 0; f < FEATURE_SIZE; f++) {
                std[f] = Math.sqrt(std[f] / Math.max(1, x.length - 1));
                if (std[f] < 1e-6) {
                    std[f] = 1.0;
                }
            }
            return std;
        }

        private double[][] normalize(double[][] x, double[] mean, double[] std) {
            double[][] out = new double[x.length][FEATURE_SIZE];
            for (int i = 0; i < x.length; i++) {
                for (int f = 0; f < FEATURE_SIZE; f++) {
                    out[i][f] = (x[i][f] - mean[f]) / std[f];
                }
            }
            return out;
        }

        private double trend(List<PowerReading> rows) {
            if (rows.size() < 2) {
                return 0;
            }
            int n = Math.min(12, rows.size() - 1);
            double sum = 0;
            for (int i = rows.size() - n; i < rows.size(); i++) {
                sum += number(rows.get(i).getPowerKw()) - number(rows.get(i - 1).getPowerKw());
            }
            return sum / n * 0.35;
        }

        private double windCurve(double windSpeed, double capacity) {
            if (windSpeed < 3.0) {
                return 0.0;
            }
            if (windSpeed >= 13.0) {
                return capacity;
            }
            double ratio = (windSpeed - 3.0) / 10.0;
            return capacity * Math.min(1.0, ratio * ratio * ratio);
        }

        private double weight(int a, int b, double scale) {
            return Math.sin((a + 1) * 12.9898 + (b + 1) * 78.233) * scale;
        }

        private List<Double> fallback(int steps, double base) {
            List<Double> values = new ArrayList<>();
            for (int i = 1; i <= steps; i++) {
                values.add(base * (1.0 + Math.sin(i / 3.0) * 0.04));
            }
            return values;
        }
    }
}
