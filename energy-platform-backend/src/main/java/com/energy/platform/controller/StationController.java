package com.energy.platform.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.energy.platform.common.PageResult;
import com.energy.platform.common.Result;
import com.energy.platform.entity.Device;
import com.energy.platform.entity.Station;
import com.energy.platform.service.DeviceService;
import com.energy.platform.service.StationService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import java.util.List;

@RestController
@RequestMapping("/api/stations")
@PreAuthorize("hasAnyAuthority('station:manage','power:view','prediction:manage','dashboard:view')")
public class StationController {
    private final StationService stationService;
    private final DeviceService deviceService;
    public StationController(StationService stationService, DeviceService deviceService) { this.stationService = stationService; this.deviceService = deviceService; }
    @GetMapping public Result<PageResult<Station>> list(@RequestParam(defaultValue="1") long page, @RequestParam(defaultValue="10") long size,
                                                        @RequestParam(required=false) String keyword) {
        QueryWrapper<Station> query = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) query.and(q -> q.like("name", keyword).or().like("code", keyword).or().like("location", keyword));
        Page<Station> result = stationService.page(new Page<>(page, size), query);
        return Result.ok(new PageResult<>(result.getRecords(), result.getTotal(), page, size));
    }
    @GetMapping("/{id}") public Result<Station> get(@PathVariable Long id) { return Result.ok(stationService.getById(id)); }
    @PostMapping @PreAuthorize("hasAuthority('station:manage')") public Result<Station> create(@RequestBody Station body) { stationService.save(body); return Result.ok(body); }
    @PutMapping("/{id}") @PreAuthorize("hasAuthority('station:manage')") public Result<Station> update(@PathVariable Long id, @RequestBody Station body) { body.setId(id); stationService.updateById(body); return Result.ok(body); }
    @DeleteMapping("/{id}") @PreAuthorize("hasAuthority('station:manage')") public Result<Boolean> delete(@PathVariable Long id) { return Result.ok(stationService.removeById(id)); }
    @GetMapping("/{id}/devices") public Result<List<Device>> devices(@PathVariable Long id) { return Result.ok(deviceService.lambdaQuery().eq(Device::getStationId, id).list()); }
}
