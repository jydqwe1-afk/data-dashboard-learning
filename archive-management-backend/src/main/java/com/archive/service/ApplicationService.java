package com.archive.service;

import com.archive.entity.AccessApplication;
import com.archive.mapper.AccessApplicationMapper;
import com.archive.mq.LogProducer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ApplicationService {
    @Autowired
    private AccessApplicationMapper appMapper;
    @Autowired
    private LogProducer logProducer;

    public Page<AccessApplication> myApplications(Long userId, int page, int size) {
        LambdaQueryWrapper<AccessApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccessApplication::getApplicantId, userId).orderByDesc(AccessApplication::getCreatedAt);
        return appMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public Page<AccessApplication> pendingReviews(Long deptId, int page, int size) {
        LambdaQueryWrapper<AccessApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccessApplication::getStatus, 0).orderByDesc(AccessApplication::getCreatedAt);
        return appMapper.selectPage(new Page<>(page, size), wrapper);
    }

    public void create(AccessApplication app) {
        app.setStatus(0);
        appMapper.insert(app);
        try { logProducer.sendLog(app.getApplicantId(), app.getArchiveId(), "apply", "提交查档申请"); } catch (Exception ignored) {}
    }

    public void review(Long id, Long reviewerId, boolean approved, String comment) {
        AccessApplication app = appMapper.selectById(id);
        app.setStatus(approved ? 1 : 2);
        app.setReviewerId(reviewerId);
        app.setReviewComment(comment);
        app.setReviewedAt(LocalDateTime.now());
        appMapper.updateById(app);
        try { logProducer.sendLog(reviewerId, app.getArchiveId(), "review",
                (approved ? "通过" : "拒绝") + "查档申请"); } catch (Exception ignored) {}
    }
}
