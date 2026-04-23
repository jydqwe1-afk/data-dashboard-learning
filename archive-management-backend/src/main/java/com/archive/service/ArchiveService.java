package com.archive.service;

import com.archive.entity.Archive;
import com.archive.mapper.ArchiveMapper;
import com.archive.mq.LogProducer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class ArchiveService {
    @Autowired
    private ArchiveMapper archiveMapper;
    @Autowired
    private LogProducer logProducer;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Page<Archive> listPage(int page, int size, Long folderId, String keyword) {
        String cacheKey = "archives:" + page + ":" + size + ":" + folderId + ":" + keyword;
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) return (Page<Archive>) cached;

        LambdaQueryWrapper<Archive> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Archive::getStatus, 1);
        if (folderId != null) wrapper.eq(Archive::getFolderId, folderId);
        if (keyword != null && !keyword.isEmpty()) wrapper.like(Archive::getTitle, keyword);
        wrapper.orderByDesc(Archive::getCreatedAt);
        Page<Archive> result = archiveMapper.selectPage(new Page<>(page, size), wrapper);
        redisTemplate.opsForValue().set(cacheKey, result, 5, TimeUnit.MINUTES);
        return result;
    }

    public Archive getById(Long id) { return archiveMapper.selectById(id); }

    public void create(Archive archive, Long userId) {
        archiveMapper.insert(archive);
        logProducer.sendLog(userId, archive.getId(), "create", "创建档案: " + archive.getTitle());
        clearCache();
    }

    public void update(Archive archive, Long userId) {
        archiveMapper.updateById(archive);
        logProducer.sendLog(userId, archive.getId(), "edit", "编辑档案: " + archive.getTitle());
        clearCache();
    }

    public void delete(Long id, Long userId) {
        Archive archive = archiveMapper.selectById(id);
        archive.setStatus(0);
        archiveMapper.updateById(archive);
        logProducer.sendLog(userId, id, "delete", "删除档案: " + archive.getTitle());
        clearCache();
    }

    private void clearCache() {
        redisTemplate.keys("archives:*").forEach(redisTemplate::delete);
    }
}
