package com.energy.platform.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.energy.platform.common.PageResult;
import com.energy.platform.common.Result;
import com.energy.platform.entity.LoginLog;
import com.energy.platform.entity.OperationLog;
import com.energy.platform.service.LoginLogService;
import com.energy.platform.service.OperationLogService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/logs")
@PreAuthorize("hasAuthority('system:manage')")
public class LogController {
    private final OperationLogService operationLogService; private final LoginLogService loginLogService;
    public LogController(OperationLogService operationLogService, LoginLogService loginLogService) { this.operationLogService = operationLogService; this.loginLogService = loginLogService; }
    @GetMapping("/operation") public Result<PageResult<OperationLog>> operation(@RequestParam(defaultValue="1") long page,@RequestParam(defaultValue="10") long size) { Page<OperationLog> r=operationLogService.page(new Page<>(page,size)); return Result.ok(new PageResult<>(r.getRecords(),r.getTotal(),page,size)); }
    @GetMapping("/login") public Result<PageResult<LoginLog>> login(@RequestParam(defaultValue="1") long page,@RequestParam(defaultValue="10") long size) { Page<LoginLog> r=loginLogService.page(new Page<>(page,size)); return Result.ok(new PageResult<>(r.getRecords(),r.getTotal(),page,size)); }
}
