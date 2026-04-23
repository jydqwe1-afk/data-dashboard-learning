package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.Department;
import com.archive.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/departments")
@PreAuthorize("hasRole('ADMIN')")
public class DepartmentController {
    @Autowired
    private DepartmentService deptService;

    @GetMapping
    public Result<?> list() { return Result.ok(deptService.listAll()); }

    @PostMapping
    public Result<?> create(@RequestBody Department dept) {
        deptService.create(dept);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Department dept) {
        dept.setId(id);
        deptService.update(dept);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        deptService.delete(id);
        return Result.ok();
    }
}
