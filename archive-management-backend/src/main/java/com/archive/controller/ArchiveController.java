package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.Archive;
import com.archive.entity.Category;
import com.archive.entity.SecurityLevel;
import com.archive.service.ArchiveService;
import com.archive.mapper.CategoryMapper;
import com.archive.mapper.SecurityLevelMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/archives")
public class ArchiveController {
    @Autowired
    private ArchiveService archiveService;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SecurityLevelMapper securityLevelMapper;

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) Long folderId,
                          @RequestParam(required = false) String keyword) {
        Page<Archive> result = archiveService.listPage(page, size, folderId, keyword);
        return Result.ok(result);
    }

    @GetMapping("/{id:\\d+}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.ok(archiveService.getById(id));
    }

    @PostMapping
    public Result<?> create(@RequestBody Archive archive, Authentication auth) {
        Long userId = (Long) auth.getDetails();
        archive.setCreatedBy(userId);
        archiveService.create(archive, userId);
        return Result.ok();
    }

    @PutMapping("/{id:\\d+}")
    public Result<?> update(@PathVariable Long id, @RequestBody Archive archive, Authentication auth) {
        archive.setId(id);
        Long userId = (Long) auth.getDetails();
        archiveService.update(archive, userId);
        return Result.ok();
    }

    @DeleteMapping("/{id:\\d+}")
    public Result<?> delete(@PathVariable Long id, Authentication auth) {
        Long userId = (Long) auth.getDetails();
        archiveService.delete(id, userId);
        return Result.ok();
    }

    @GetMapping("/categories")
    public Result<?> categories() {
        return Result.ok(categoryMapper.selectList(new LambdaQueryWrapper<Category>()));
    }

    @GetMapping("/security-levels")
    public Result<?> securityLevels() {
        return Result.ok(securityLevelMapper.selectList(new LambdaQueryWrapper<SecurityLevel>().orderByAsc(SecurityLevel::getLevelOrder)));
    }
}
