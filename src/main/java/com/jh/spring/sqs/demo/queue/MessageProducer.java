package com.jh.spring.sqs.demo.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.SqsMessageHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.UUID;

@RequiredArgsConstructor
public class MessageProducer {

    private final String queueName;
    private final QueueMessagingTemplate queueMessagingTemplate;

    public void send(Integer groupId, String messagePayload) {
        Message<String> msg = MessageBuilder
                .withPayload(messagePayload)
                .setHeader(SqsMessageHeaders.SQS_GROUP_ID_HEADER, groupId.toString())
                .setHeader(SqsMessageHeaders.SQS_DEDUPLICATION_ID_HEADER, UUID.randomUUID().toString())
                .build();

        queueMessagingTemplate.send(queueName, msg);
    }

}
