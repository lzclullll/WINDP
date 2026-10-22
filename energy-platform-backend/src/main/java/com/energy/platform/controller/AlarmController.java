package com.energy.platform.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.energy.platform.common.PageResult;
import com.energy.platform.common.Result;
import com.energy.platform.entity.Alarm;
import com.energy.platform.entity.WorkOrder;
import com.energy.platform.service.AlarmService;
import com.energy.platform.service.WorkOrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/alarms")
@PreAuthorize("hasAuthority('alarm:manage')")
public class AlarmController {
    private final AlarmService service; private final WorkOrderService workOrderService;
    public AlarmController(AlarmService service, WorkOrderService workOrderService) { this.service = service; this.workOrderService = workOrderService; }
    @GetMapping public Result<PageResult<Alarm>> list(@RequestParam(defaultValue="1") long page, @RequestParam(defaultValue="10") long size) { Page<Alarm> r=service.page(new Page<>(page,size)); return Result.ok(new PageResult<>(r.getRecords(),r.getTotal(),page,size)); }
    @GetMapping("/{id}") public Result<Alarm> get(@PathVariable Long id) { return Result.ok(service.getById(id)); }
    @PostMapping public Result<Alarm> create(@RequestBody Alarm body) { service.save(body); return Result.ok(body); }
    @PutMapping("/{id}") public Result<Alarm> update(@PathVariable Long id,@RequestBody Alarm body) { body.setId(id); service.updateById(body); return Result.ok(body); }
    @DeleteMapping("/{id}") public Result<Boolean> delete(@PathVariable Long id) { return Result.ok(service.removeById(id)); }
    @PostMapping("/{id}/handle") public Result<Boolean> handle(@PathVariable Long id) { Alarm a=service.getById(id); a.setStatus("HANDLED"); a.setHandledAt(LocalDateTime.now()); return Result.ok(service.updateById(a)); }
    @PostMapping("/{id}/create-work-order") public Result<WorkOrder> createWorkOrder(@PathVariable Long id) { Alarm a=service.getById(id); WorkOrder w=new WorkOrder(); w.setTitle("告警工单-"+a.getTitle()); w.setDescription(a.getContent()); w.setStationId(a.getStationId()); w.setDeviceId(a.getDeviceId()); w.setType("故障维修"); w.setPriority("HIGH"); w.setStatus("PENDING"); workOrderService.save(w); return Result.ok(w); }
    @GetMapping("/statistics") public Result<Map<String,Object>> statistics() { return Result.ok(Map.of("open", service.lambdaQuery().eq(Alarm::getStatus,"OPEN").count(), "handled", service.lambdaQuery().eq(Alarm::getStatus,"HANDLED").count())); }
}
