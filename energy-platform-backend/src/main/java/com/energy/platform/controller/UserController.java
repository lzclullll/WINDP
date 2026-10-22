package com.energy.platform.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.energy.platform.common.PageResult;
import com.energy.platform.common.Result;
import com.energy.platform.entity.User;
import com.energy.platform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasAuthority('system:manage')")
public class UserController {
    private final UserService service;
    private final PasswordEncoder passwordEncoder;
    public UserController(UserService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping
    public Result<PageResult<User>> list(@RequestParam(defaultValue = "1") long page, @RequestParam(defaultValue = "10") long size, @RequestParam(required = false) String keyword) {
        QueryWrapper<User> query = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            query.and(q -> q.like("username", keyword).or().like("real_name", keyword));
        }
        Page<User> result = service.page(new Page<>(page, size), query);
        return Result.ok(new PageResult<>(result.getRecords(), result.getTotal(), page, size));
    }
    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Long id) { return Result.ok(service.getById(id)); }
    @PostMapping
    public Result<User> create(@Valid @RequestBody User body) {
        if (!StringUtils.hasText(body.getPassword())) body.setPassword("123456");
        body.setPassword(passwordEncoder.encode(body.getPassword()));
        if (!StringUtils.hasText(body.getStatus())) body.setStatus("ENABLED");
        service.save(body);
        return Result.ok(body);
    }
    @PutMapping("/{id}")
    public Result<User> update(@PathVariable Long id, @Valid @RequestBody User body) {
        body.setId(id);
        if (StringUtils.hasText(body.getPassword())) body.setPassword(passwordEncoder.encode(body.getPassword()));
        else body.setPassword(null);
        service.updateById(body);
        return Result.ok(body);
    }
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) { return Result.ok(service.removeById(id)); }
}
