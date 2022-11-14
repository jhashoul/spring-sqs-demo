package com.jh.spring.sqs.demo.queue;

import com.jh.spring.sqs.demo.services.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageConsumer {
    private final DemoService demoService;

//    @SqsListener(value = "${queue.demo-service.url}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    public void receiveMessage(String message, @Header("MessageGroupId") String groupId) {
//        demoService.process(message, groupId);
//    }

}