package com.energy.platform.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.energy.platform.common.PageResult;
import com.energy.platform.common.Result;
import com.energy.platform.common.BusinessException;
import com.energy.platform.entity.WorkOrder;
import com.energy.platform.service.WorkOrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/work-orders")
@PreAuthorize("hasAnyAuthority('workorder:view','workorder:manage')")
public class WorkOrderController {
    private final WorkOrderService service;
    public WorkOrderController(WorkOrderService service) { this.service = service; }
    @GetMapping public Result<PageResult<WorkOrder>> list(@RequestParam(defaultValue="1") long page, @RequestParam(defaultValue="10") long size) { Page<WorkOrder> r = service.page(new Page<>(page,size)); return Result.ok(new PageResult<>(r.getRecords(),r.getTotal(),page,size)); }
    @GetMapping("/{id}") public Result<WorkOrder> get(@PathVariable Long id) { return Result.ok(service.getById(id)); }
    @PostMapping @PreAuthorize("hasAuthority('workorder:manage')") public Result<WorkOrder> create(@RequestBody WorkOrder body) { service.save(body); return Result.ok(body); }
    @PutMapping("/{id}") @PreAuthorize("hasAuthority('workorder:manage')") public Result<WorkOrder> update(@PathVariable Long id,@RequestBody WorkOrder body) { body.setId(id); service.updateById(body); return Result.ok(body); }
    @DeleteMapping("/{id}") @PreAuthorize("hasAuthority('workorder:manage')") public Result<Boolean> delete(@PathVariable Long id) { return Result.ok(service.removeById(id)); }
    @PostMapping("/{id}/assign") @PreAuthorize("hasAuthority('workorder:manage')") public Result<Boolean> assign(@PathVariable Long id,@RequestBody Map<String,Long> body) { WorkOrder w=service.getById(id); w.setAssigneeId(body.get("assigneeId")); w.setStatus("PROCESSING"); return Result.ok(service.updateById(w)); }
    @PostMapping("/{id}/complete") @PreAuthorize("hasAnyAuthority('workorder:resolve','workorder:manage')") public Result<Boolean> complete(@PathVariable Long id) { WorkOrder w=service.getById(id); if (w == null) throw new BusinessException("工单不存在"); if ("RESOLVED".equals(w.getStatus()) || "CLOSED".equals(w.getStatus())) throw new BusinessException("该工单已经处理完成"); w.setStatus("RESOLVED"); w.setCompletedAt(LocalDateTime.now()); return Result.ok(service.updateById(w)); }
    @PostMapping("/{id}/close") @PreAuthorize("hasAuthority('workorder:manage')") public Result<Boolean> close(@PathVariable Long id) { WorkOrder w=service.getById(id); w.setStatus("CLOSED"); return Result.ok(service.updateById(w)); }
    @GetMapping("/statistics") public Result<Map<String,Object>> statistics() { return Result.ok(Map.of("pending", service.lambdaQuery().eq(WorkOrder::getStatus,"PENDING").count(), "processing", service.lambdaQuery().eq(WorkOrder::getStatus,"PROCESSING").count())); }
}
