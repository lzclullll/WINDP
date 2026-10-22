package com.energy.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.energy.platform.common.PageResult;
import com.energy.platform.common.Result;
import com.energy.platform.entity.Permission;
import com.energy.platform.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
@PreAuthorize("hasAuthority('system:manage')")
public class PermissionController {
    private final PermissionService service;
    public PermissionController(PermissionService service) { this.service = service; }
    @GetMapping
    public Result<PageResult<Permission>> list(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size, @RequestParam(required = false) String keyword) {
        QueryWrapper<Permission> query = new QueryWrapper<>();
        Page<Permission> result = service.page(new Page<>(page, size), query);
        return Result.ok(new PageResult<>(result.getRecords(), result.getTotal(), page, size));
    }
    @GetMapping("/{id}")
    public Result<Permission> get(@PathVariable Long id) { return Result.ok(service.getById(id)); }
    @PostMapping
    public Result<Permission> create(@Valid @RequestBody Permission body) { service.save(body); return Result.ok(body); }
    @PutMapping("/{id}")
    public Result<Permission> update(@PathVariable Long id, @Valid @RequestBody Permission body) { body.setId(id); service.updateById(body); return Result.ok(body); }
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) { return Result.ok(service.removeById(id)); }
}
