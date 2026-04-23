package com.archive.controller;

import com.archive.common.Result;
import com.archive.entity.Folder;
import com.archive.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/archive/folders")
public class FolderController {
    @Autowired
    private FolderService folderService;

    @GetMapping
    public Result<?> list() { return Result.ok(folderService.listAll()); }

    @PostMapping
    public Result<?> create(@RequestBody Folder folder) {
        folderService.create(folder);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Folder folder) {
        folder.setId(id);
        folderService.update(folder);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        folderService.delete(id);
        return Result.ok();
    }
}
