package com.energy.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.energy.platform.common.PageResult;
import com.energy.platform.common.Result;
import com.energy.platform.entity.AlarmRule;
import com.energy.platform.service.AlarmRuleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alarm-rules")
@PreAuthorize("hasAuthority('alarm:manage')")
public class AlarmRuleController {
    private final AlarmRuleService service;
    public AlarmRuleController(AlarmRuleService service) { this.service = service; }
    @GetMapping
    public Result<PageResult<AlarmRule>> list(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size, @RequestParam(required = false) String keyword) {
        QueryWrapper<AlarmRule> query = new QueryWrapper<>();
        Page<AlarmRule> result = service.page(new Page<>(page, size), query);
        return Result.ok(new PageResult<>(result.getRecords(), result.getTotal(), page, size));
    }
    @GetMapping("/{id}")
    public Result<AlarmRule> get(@PathVariable Long id) { return Result.ok(service.getById(id)); }
    @PostMapping
    public Result<AlarmRule> create(@Valid @RequestBody AlarmRule body) { service.save(body); return Result.ok(body); }
    @PutMapping("/{id}")
    public Result<AlarmRule> update(@PathVariable Long id, @Valid @RequestBody AlarmRule body) { body.setId(id); service.updateById(body); return Result.ok(body); }
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) { return Result.ok(service.removeById(id)); }
}
