package com.archive.service;

import com.archive.entity.Archive;
import com.archive.mapper.ArchiveMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchService {
    @Autowired
    private ArchiveMapper archiveMapper;

    public Page<Archive> search(int page, int size, String keyword, String securityLevel,
                                 String creator, String startDate, String endDate) {
        LambdaQueryWrapper<Archive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Archive::getStatus, 1);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.and(w -> w.like(Archive::getTitle, keyword));
        }
        wrapper.orderByDesc(Archive::getCreatedAt);
        return archiveMapper.selectPage(new Page<>(page, size), wrapper);
    }
}
