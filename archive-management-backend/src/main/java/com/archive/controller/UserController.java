package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.User;
import com.archive.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String keyword) {
        Page<User> result = userService.listPage(page, size, keyword);
        return Result.ok(result);
    }

    @PostMapping
    public Result<?> create(@RequestBody User user) {
        if (userService.getByUsername(user.getUsername()) != null) {
            return Result.error("用户名已存在");
        }
        userService.create(user);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.ok();
    }
}
