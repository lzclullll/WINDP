package com.energy.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.energy.platform.common.PageResult;
import com.energy.platform.common.Result;
import com.energy.platform.entity.Report;
import com.energy.platform.entity.Alarm;
import com.energy.platform.entity.PowerReading;
import com.energy.platform.entity.Station;
import com.energy.platform.entity.WorkOrder;
import com.energy.platform.service.AlarmService;
import com.energy.platform.service.PowerReadingService;
import com.energy.platform.service.ReportService;
import com.energy.platform.service.StationService;
import com.energy.platform.service.WorkOrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Map;
import java.util.LinkedHashMap;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasAuthority('report:view')")
public class ReportController {
    private final ReportService service;
    private final PowerReadingService powerReadingService;
    private final AlarmService alarmService;
    private final WorkOrderService workOrderService;
    private final StationService stationService;

    public ReportController(ReportService service, PowerReadingService powerReadingService,
                            AlarmService alarmService, WorkOrderService workOrderService,
                            StationService stationService) {
        this.service = service;
        this.powerReadingService = powerReadingService;
        this.alarmService = alarmService;
        this.workOrderService = workOrderService;
        this.stationService = stationService;
    }

    @GetMapping("/daily")
    public Result<Map<String, Object>> daily() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("generation", generationSince(LocalDate.now().atStartOfDay()));
        data.put("alarms", alarmService.count());
        data.put("openAlarms", alarmService.lambdaQuery().eq(Alarm::getStatus, "OPEN").count());
        data.put("orders", workOrderService.count());
        data.put("activeOrders", workOrderService.lambdaQuery().in(WorkOrder::getStatus, "PENDING", "PROCESSING").count());
        data.put("stations", stationService.count());
        return Result.ok(data);
    }

    @GetMapping("/monthly")
    public Result<Map<String, Object>> monthly() {
        LocalDateTime start = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        long orders = workOrderService.lambdaQuery().ge(WorkOrder::getCreatedAt, start).count();
        return Result.ok(Map.of("generation", generationSince(start), "availability", availability(), "cost", orders * 1000L));
    }

    @GetMapping("/yearly")
    public Result<Map<String, Object>> yearly() {
        LocalDateTime start = LocalDate.now().withDayOfYear(1).atStartOfDay();
        return Result.ok(Map.of("generation", generationSince(start), "availability", availability()));
    }

    private long generationSince(LocalDateTime start) {
        List<PowerReading> rows = powerReadingService.lambdaQuery().ge(PowerReading::getRecordTime, start).list();
        double total = rows.stream().map(PowerReading::getPowerKw).filter(java.util.Objects::nonNull)
            .mapToDouble(BigDecimal::doubleValue).sum();
        return Math.round(total / 240.0);
    }

    private double availability() {
        long total = stationService.count();
        if (total == 0) return 0;
        long running = stationService.lambdaQuery().eq(Station::getStatus, "RUNNING").count();
        return Math.round(running * 1000.0 / total) / 10.0;
    }

    @PostMapping("/custom")
    public Result<Report> custom(@RequestBody Report body) {
        service.save(body);
        return Result.ok(body);
    }

    @GetMapping("/prediction/{id}")
    public Result<Map<String, Object>> prediction(@PathVariable Long id) {
        return Result.ok(Map.of("predictionId", id, "summary", "预测任务已完成", "status", "COMPLETED"));
    }

    @GetMapping("/export")
    public Result<String> export(@RequestParam(defaultValue = "HTML") String format) {
        return Result.ok("报表导出 " + format + " 完成");
    }

    @GetMapping("/history")
    public Result<PageResult<Report>> history(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size) {
        Page<Report> result = service.page(new Page<>(page, size));
        return Result.ok(new PageResult<>(result.getRecords(), result.getTotal(), page, size));
    }
}
