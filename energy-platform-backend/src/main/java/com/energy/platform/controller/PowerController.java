package com.energy.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.energy.platform.common.PageResult;
import com.energy.platform.common.Result;
import com.energy.platform.entity.PowerReading;
import com.energy.platform.entity.Alarm;
import com.energy.platform.entity.Station;
import com.energy.platform.entity.WorkOrder;
import com.energy.platform.service.AlarmService;
import com.energy.platform.service.PowerReadingService;
import com.energy.platform.service.StationService;
import com.energy.platform.service.WorkOrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/power")
@PreAuthorize("hasAnyAuthority('power:view','dashboard:view')")
public class PowerController {
    private final PowerReadingService service;
    private final StationService stationService;
    private final AlarmService alarmService;
    private final WorkOrderService workOrderService;
    public PowerController(PowerReadingService service, StationService stationService,
                           AlarmService alarmService, WorkOrderService workOrderService) {
        this.service = service;
        this.stationService = stationService;
        this.alarmService = alarmService;
        this.workOrderService = workOrderService;
    }
    @GetMapping("/real-time")
    public Result<List<PowerReading>> realTime(@RequestParam(required=false) Long stationId) {
        LambdaQueryWrapper<PowerReading> q = new LambdaQueryWrapper<PowerReading>().orderByDesc(PowerReading::getRecordTime).last("LIMIT 50");
        if (stationId != null) q.eq(PowerReading::getStationId, stationId);
        return Result.ok(service.list(q));
    }
    @GetMapping("/history")
    public Result<PageResult<PowerReading>> history(@RequestParam(defaultValue="1") long page, @RequestParam(defaultValue="20") long size, @RequestParam(required=false) Long stationId) {
        LambdaQueryWrapper<PowerReading> q = new LambdaQueryWrapper<PowerReading>().orderByDesc(PowerReading::getRecordTime);
        if (stationId != null) q.eq(PowerReading::getStationId, stationId);
        Page<PowerReading> r = service.page(new Page<>(page,size), q);
        return Result.ok(new PageResult<>(r.getRecords(), r.getTotal(), page, size));
    }
    @PostMapping("/upload") @PreAuthorize("hasAuthority('station:manage')") public Result<PowerReading> upload(@RequestBody PowerReading body) { service.save(body); return Result.ok(body); }
    @GetMapping("/statistics") public Result<Map<String,Object>> statistics() {
        List<PowerReading> rows = service.lambdaQuery().ge(PowerReading::getRecordTime, LocalDateTime.now().minusDays(1)).list();
        double accumulatedPower = rows.stream().map(PowerReading::getPowerKw).filter(Objects::nonNull).mapToDouble(BigDecimal::doubleValue).sum();
        List<Station> stations = stationService.list();
        List<Map<String, Object>> stationPowers = new ArrayList<>();
        double currentPower = 0;
        Map<String, Long> stationStatus = new LinkedHashMap<>();
        stationStatus.put("RUNNING", 0L);
        stationStatus.put("MAINTENANCE", 0L);
        stationStatus.put("STOPPED", 0L);
        for (Station station : stations) {
            stationStatus.compute(station.getStatus(), (key, value) -> value == null ? 1L : value + 1);
            PowerReading latest = service.lambdaQuery()
                .eq(PowerReading::getStationId, station.getId())
                .orderByDesc(PowerReading::getRecordTime)
                .last("LIMIT 1")
                .one();
            double power = latest == null || latest.getPowerKw() == null ? 0 : latest.getPowerKw().doubleValue();
            currentPower += power;
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("stationId", station.getId());
            item.put("stationName", station.getName());
            item.put("powerKw", Math.round(power));
            stationPowers.add(item);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("todayGeneration", Math.round(accumulatedPower / 240.0));
        data.put("totalPower", Math.round(currentPower));
        data.put("samples", rows.size());
        data.put("stationCount", stations.size());
        data.put("alarmCount", alarmService.count());
        data.put("openAlarmCount", alarmService.lambdaQuery().eq(Alarm::getStatus, "OPEN").count());
        data.put("workOrderCount", workOrderService.count());
        data.put("activeWorkOrderCount", workOrderService.lambdaQuery().in(WorkOrder::getStatus, "PENDING", "PROCESSING").count());
        data.put("stationStatus", stationStatus);
        data.put("stationPowers", stationPowers);
        return Result.ok(data);
    }
    @GetMapping("/export") public Result<String> export() { return Result.ok("模拟导出完成，可扩展为 Excel 文件流"); }
    @GetMapping("/compare") public Result<Map<String,Object>> compare() { return Result.ok(Map.of("stations", List.of("张北风电一场","格尔木光伏站"), "values", List.of(1680,1260))); }
}
