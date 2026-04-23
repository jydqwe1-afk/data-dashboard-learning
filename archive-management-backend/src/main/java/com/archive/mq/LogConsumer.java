package com.archive.mq;

import com.archive.common.Constants;
import com.archive.entity.OperationLog;
import com.archive.mapper.OperationLogMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogConsumer {
    @Autowired
    private OperationLogMapper logMapper;

    @RabbitListener(queues = Constants.MQ_LOG_QUEUE)
    public void handleLog(OperationLog log) {
        logMapper.insert(log);
    }
}
