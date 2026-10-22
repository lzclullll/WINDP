package com.energy.platform.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.energy.platform.common.BusinessException;
import com.energy.platform.common.PageResult;
import com.energy.platform.common.Result;
import com.energy.platform.dto.PredictionRequest;
import com.energy.platform.entity.PredictionResult;
import com.energy.platform.entity.PredictionTask;
import com.energy.platform.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/predict")
@PreAuthorize("hasAuthority('prediction:manage')")
public class PredictionController {
    private final PredictionTaskService taskService;
    private final PredictionResultService resultService;
    private final PredictionEngineService engineService;
    public PredictionController(PredictionTaskService taskService, PredictionResultService resultService,
                                PredictionEngineService engineService) {
        this.taskService = taskService; this.resultService = resultService; this.engineService = engineService;
    }
    @GetMapping("/{id}") public Result<PredictionResult> get(@PathVariable Long id) { return Result.ok(resultService.getById(id)); }
    @GetMapping("/history") public Result<PageResult<PredictionResult>> history(@RequestParam(defaultValue="1") long page,@RequestParam(defaultValue="10") long size) { Page<PredictionResult> r=resultService.lambdaQuery().orderByDesc(PredictionResult::getPredictionTime).page(new Page<>(page,size)); return Result.ok(new PageResult<>(r.getRecords(),r.getTotal(),page,size)); }
    @GetMapping("/tasks") public Result<PageResult<PredictionTask>> tasks(@RequestParam(defaultValue="1") long page,@RequestParam(defaultValue="10") long size) { Page<PredictionTask> r=taskService.page(new Page<>(page,size)); return Result.ok(new PageResult<>(r.getRecords(),r.getTotal(),page,size)); }
    @GetMapping("/tasks/{id}") public Result<PredictionTask> task(@PathVariable Long id) { return Result.ok(taskService.getById(id)); }
    @PostMapping("/tasks") public Result<PredictionTask> createTask(@RequestBody PredictionTask body) { body.setStatus("PENDING"); taskService.save(body); return Result.ok(body); }
    @PostMapping("/tasks/{id}/run")
    public Result<PredictionResult> runTask(@PathVariable Long id) {
        PredictionTask task = taskService.getById(id);
        if (task == null) throw new BusinessException("预测任务不存在");
        if (!"PENDING".equals(task.getStatus()) && !"FAILED".equals(task.getStatus())) {
            throw new BusinessException("只有待运行或失败的任务可以运行");
        }

        LocalDateTime startTime = LocalDateTime.now();
        task.setStatus("RUNNING");
        task.setStartTime(startTime);
        task.setEndTime(null);
        taskService.updateById(task);

        PredictionRequest request = new PredictionRequest();
        request.setStationId(task.getStationId());
        request.setModelType(task.getModelType());
        request.setHorizon(task.getHorizon());
        request.setConfidenceLevel(task.getConfidenceLevel());
        request.setStartTime(startTime);
        request.setEndTime(startTime.plusHours(task.getHorizon()));
        try {
            PredictionResult prediction = engineService.predict(request, task.getId());
            resultService.save(prediction);
            task.setStatus("COMPLETED");
            task.setEndTime(LocalDateTime.now());
            taskService.updateById(task);
            return Result.ok(prediction);
        } catch (RuntimeException exception) {
            task.setStatus("FAILED");
            task.setEndTime(LocalDateTime.now());
            task.setErrorMessage("预测执行失败");
            taskService.updateById(task);
            throw exception;
        }
    }
    @PutMapping("/tasks/{id}") public Result<PredictionTask> updateTask(@PathVariable Long id,@RequestBody PredictionTask body) { body.setId(id); taskService.updateById(body); return Result.ok(body); }
    @DeleteMapping("/tasks/{id}") public Result<Boolean> deleteTask(@PathVariable Long id) { return Result.ok(taskService.removeById(id)); }
}
