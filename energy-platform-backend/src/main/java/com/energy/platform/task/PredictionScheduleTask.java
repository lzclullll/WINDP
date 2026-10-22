package com.energy.platform.task;


import com.energy.platform.dto.PredictionRequest;
import com.energy.platform.entity.PredictionResult;
import com.energy.platform.entity.PredictionTask;
import com.energy.platform.service.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Component
@ConditionalOnProperty(name = "prediction.scheduler.enabled", havingValue = "true")
public class PredictionScheduleTask {
    private final PredictionTaskService taskService; private final PredictionResultService resultService; private final PredictionEngineService engineService;
    public PredictionScheduleTask(PredictionTaskService taskService, PredictionResultService resultService, PredictionEngineService engineService) { this.taskService = taskService; this.resultService = resultService; this.engineService = engineService; }
    @Scheduled(fixedDelay = 120000)
    public void executePending() {
        for (PredictionTask task : taskService.lambdaQuery().eq(PredictionTask::getStatus, "PENDING").list()) {
            try {
                task.setStatus("RUNNING"); taskService.updateById(task);
                PredictionRequest req = new PredictionRequest(); req.setStationId(task.getStationId()); req.setModelType(task.getModelType()); req.setHorizon(task.getHorizon()); req.setConfidenceLevel(task.getConfidenceLevel());
                PredictionResult result = engineService.predict(req, task.getId()); resultService.save(result);
                task.setStatus("COMPLETED");
            } catch (Exception e) { task.setStatus("FAILED"); task.setErrorMessage(e.getMessage()); }
            taskService.updateById(task);
        }
    }
}
