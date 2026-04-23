package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.*;
import com.archive.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired private ArchiveMapper archiveMapper;
    @Autowired private AccessApplicationMapper appMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private CategoryMapper categoryMapper;
    @Autowired private SecurityLevelMapper securityLevelMapper;

    @GetMapping("/stats")
    public Result<?> stats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalArchives", archiveMapper.selectCount(new LambdaQueryWrapper<Archive>().eq(Archive::getStatus, 1)));
        stats.put("pendingReview", appMapper.selectCount(new LambdaQueryWrapper<AccessApplication>().eq(AccessApplication::getStatus, 0)));
        stats.put("totalUsers", userMapper.selectCount(null));
        return Result.ok(stats);
    }

    @GetMapping("/categories")
    public Result<?> categoryStats() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<Category> categories = categoryMapper.selectList(null);
        for (Category c : categories) {
            Map<String, Object> item = new HashMap<>();
            item.put("type", c.getName());
            item.put("value", archiveMapper.selectCount(
                    new LambdaQueryWrapper<Archive>().eq(Archive::getCategoryId, c.getId()).eq(Archive::getStatus, 1)));
            result.add(item);
        }
        return Result.ok(result);
    }

    @GetMapping("/security-distribution")
    public Result<?> securityDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();
        List<SecurityLevel> levels = securityLevelMapper.selectList(
                new LambdaQueryWrapper<SecurityLevel>().orderByAsc(SecurityLevel::getLevelOrder));
        for (SecurityLevel sl : levels) {
            Map<String, Object> item = new HashMap<>();
            item.put("type", sl.getName());
            item.put("value", archiveMapper.selectCount(
                    new LambdaQueryWrapper<Archive>().eq(Archive::getSecurityLevelId, sl.getId()).eq(Archive::getStatus, 1)));
            result.add(item);
        }
        return Result.ok(result);
    }
}
