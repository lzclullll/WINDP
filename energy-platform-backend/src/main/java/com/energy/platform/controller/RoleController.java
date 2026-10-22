package com.energy.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.energy.platform.common.PageResult;
import com.energy.platform.common.Result;
import com.energy.platform.entity.Role;
import com.energy.platform.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("hasAuthority('system:manage')")
public class RoleController {
    private final RoleService service;
    public RoleController(RoleService service) { this.service = service; }
    @GetMapping
    public Result<PageResult<Role>> list(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size, @RequestParam(required = false) String keyword) {
        QueryWrapper<Role> query = new QueryWrapper<>();
        Page<Role> result = service.page(new Page<>(page, size), query);
        return Result.ok(new PageResult<>(result.getRecords(), result.getTotal(), page, size));
    }
    @GetMapping("/{id}")
    public Result<Role> get(@PathVariable Long id) { return Result.ok(service.getById(id)); }
    @PostMapping
    public Result<Role> create(@Valid @RequestBody Role body) { service.save(body); return Result.ok(body); }
    @PutMapping("/{id}")
    public Result<Role> update(@PathVariable Long id, @Valid @RequestBody Role body) { body.setId(id); service.updateById(body); return Result.ok(body); }
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) { return Result.ok(service.removeById(id)); }
}
