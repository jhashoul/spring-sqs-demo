package com.jh.spring.sqs.demo.queue;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.util.CollectionUtils;
import com.jh.spring.sqs.demo.services.DemoService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class MessageConsumerOrderGuarantor {

    private static final int WAIT_DURATION_SEC = 1;
    private AtomicBoolean runningState = new AtomicBoolean(false);
    private final String queueName;
    private final DemoService demoService;
    private final AmazonSQSAsync amazonSQSAsync;

    @PostConstruct
    public void init() {
        ForkJoinPool.commonPool().execute(this::start);
        ForkJoinPool.commonPool().execute(this::start);
        ForkJoinPool.commonPool().execute(this::start);
    }

    public void start() {
        runningState.set(true);
        do {
            Optional<Message> retMessage = getMessage();
            if (retMessage.isPresent()) {
                Message message = retMessage.get();
                String payload = message.getBody();
                String groupId = message.getAttributes().get("MessageGroupId");
                demoService.process(payload, groupId);
                deleteMessage(message);
            }
        } while (runningState.get());
    }

    @PreDestroy
    public void stop() {
        runningState.set(false);
    }

    private Optional<Message> getMessage() {
        ReceiveMessageResult receiveMessageResult = amazonSQSAsync
                .receiveMessage(new ReceiveMessageRequest(queueName)
                        .withMaxNumberOfMessages(1)
                        .withAttributeNames("All")
                        .withMessageAttributeNames("All")
                        .withWaitTimeSeconds(WAIT_DURATION_SEC));
        return CollectionUtils.isNullOrEmpty(receiveMessageResult.getMessages()) ? Optional.empty() : Optional.of(receiveMessageResult.getMessages().get(0));
    }

    private void deleteMessage(Message message) {
        amazonSQSAsync.deleteMessage(new DeleteMessageRequest(queueName, message.getReceiptHandle()));
    }

}