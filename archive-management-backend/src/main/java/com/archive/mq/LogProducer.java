package com.archive.mq;

import com.archive.common.Constants;
import com.archive.entity.OperationLog;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendLog(Long userId, Long archiveId, String type, String details) {
        OperationLog log = new OperationLog();
        log.setUserId(userId);
        log.setArchiveId(archiveId);
        log.setOperationType(type);
        log.setDetails(details);
        rabbitTemplate.convertAndSend(Constants.MQ_LOG_EXCHANGE, Constants.MQ_LOG_ROUTING_KEY, log);
    }
}
