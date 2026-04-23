package com.archive.service;

import com.archive.entity.OperationLog;
import com.archive.mapper.OperationLogMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    @Autowired
    private OperationLogMapper logMapper;

    public Page<OperationLog> listPage(int page, int size, String operator, String type) {
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<>();
        if (operator != null && !operator.isEmpty()) wrapper.like(OperationLog::getUserId, operator);
        if (type != null && !type.isEmpty()) wrapper.eq(OperationLog::getOperationType, type);
        wrapper.orderByDesc(OperationLog::getOperationTime);
        return logMapper.selectPage(new Page<>(page, size), wrapper);
    }
}
