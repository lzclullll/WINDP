package com.energy.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.energy.platform.common.PageResult;
import com.energy.platform.common.Result;
import com.energy.platform.entity.PredictionModel;
import com.energy.platform.service.PredictionModelCrudService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/models")
@PreAuthorize("hasAuthority('prediction:manage')")
public class ModelController {
    private final PredictionModelCrudService service;
    public ModelController(PredictionModelCrudService service) { this.service = service; }
    @GetMapping
    public Result<PageResult<PredictionModel>> list(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size, @RequestParam(required = false) String keyword) {
        QueryWrapper<PredictionModel> query = new QueryWrapper<>();
        Page<PredictionModel> result = service.page(new Page<>(page, size), query);
        return Result.ok(new PageResult<>(result.getRecords(), result.getTotal(), page, size));
    }
    @GetMapping("/compare")
    public Result<List<Map<String, Object>>> compare() {
        return Result.ok(List.of(
            Map.of("modelType", "Transformer", "rmse", 38.6, "mae", 27.4, "mape", 4.3),
            Map.of("modelType", "LSTM", "rmse", 39.8, "mae", 29.7, "mape", 4.2),
            Map.of("modelType", "LightGBM", "rmse", 42.3, "mae", 31.1, "mape", 4.8),
            Map.of("modelType", "ARIMA", "rmse", 55.6, "mae", 41.2, "mape", 6.7)
        ));
    }
    @GetMapping("/{id}")
    public Result<PredictionModel> get(@PathVariable Long id) { return Result.ok(service.getById(id)); }
    @PostMapping
    public Result<PredictionModel> create(@Valid @RequestBody PredictionModel body) { service.save(body); return Result.ok(body); }
    @PutMapping("/{id}")
    public Result<PredictionModel> update(@PathVariable Long id, @Valid @RequestBody PredictionModel body) { body.setId(id); service.updateById(body); return Result.ok(body); }
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) { return Result.ok(service.removeById(id)); }
}
