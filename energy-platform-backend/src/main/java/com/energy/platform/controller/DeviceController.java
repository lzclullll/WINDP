package com.energy.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.energy.platform.common.PageResult;
import com.energy.platform.common.Result;
import com.energy.platform.entity.Device;
import com.energy.platform.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
@PreAuthorize("hasAnyAuthority('device:manage','station:manage')")
public class DeviceController {
    private final DeviceService service;
    public DeviceController(DeviceService service) { this.service = service; }
    @GetMapping
    public Result<PageResult<Device>> list(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size, @RequestParam(required = false) String keyword) {
        QueryWrapper<Device> query = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) query.and(q -> q.like("device_name", keyword).or().like("device_code", keyword));
        Page<Device> result = service.page(new Page<>(page, size), query);
        return Result.ok(new PageResult<>(result.getRecords(), result.getTotal(), page, size));
    }
    @GetMapping("/{id}")
    public Result<Device> get(@PathVariable Long id) { return Result.ok(service.getById(id)); }
    @PostMapping
    public Result<Device> create(@Valid @RequestBody Device body) { service.save(body); return Result.ok(body); }
    @PutMapping("/{id}")
    public Result<Device> update(@PathVariable Long id, @Valid @RequestBody Device body) { body.setId(id); service.updateById(body); return Result.ok(body); }
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) { return Result.ok(service.removeById(id)); }
}
