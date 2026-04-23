package com.archive.controller;

import com.archive.common.Result;
import com.archive.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/logs")
@PreAuthorize("hasRole('ADMIN')")
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String operator,
                          @RequestParam(required = false) String type) {
        return Result.ok(logService.listPage(page, size, operator, type));
    }
}
