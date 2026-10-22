package com.energy.platform.task;


import com.energy.platform.entity.PowerReading;
import com.energy.platform.entity.Station;
import com.energy.platform.service.PowerReadingService;
import com.energy.platform.service.StationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataCollectTask {
    private final StationService stationService; private final PowerReadingService powerService; private final Random random = new Random();
    public DataCollectTask(StationService stationService, PowerReadingService powerService) { this.stationService = stationService; this.powerService = powerService; }
    @Scheduled(fixedDelay = 15000, initialDelay = 15000)
    public void collect() {
        LocalDateTime now = LocalDateTime.now();
        int alignedSecond = (now.getSecond() / 15) * 15;
        LocalDateTime recordTime = now.withSecond(alignedSecond).withNano(0);
        for (Station s : stationService.list()) {
            PowerReading r = new PowerReading();
            r.setStationId(s.getId()); r.setRecordTime(recordTime);
            double base = s.getCapacity() == null ? 1000 : s.getCapacity().doubleValue() / 100;
            r.setPowerKw(BigDecimal.valueOf(Math.max(0, base * (0.55 + random.nextDouble() * 0.25))));
            r.setWindSpeed(BigDecimal.valueOf(4 + random.nextDouble() * 8));
            r.setIrradiance(BigDecimal.valueOf("SOLAR".equals(s.getType()) ? 300 + random.nextDouble() * 600 : random.nextDouble() * 100));
            r.setTemperature(BigDecimal.valueOf(18 + random.nextDouble() * 12));
            r.setHumidity(BigDecimal.valueOf(45 + random.nextDouble() * 30));
            r.setDeviceStatus("NORMAL"); r.setDataQuality("GOOD"); powerService.save(r);
        }
    }

}
