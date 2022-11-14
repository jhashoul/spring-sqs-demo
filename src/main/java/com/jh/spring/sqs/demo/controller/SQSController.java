package com.jh.spring.sqs.demo.controller;

import com.jh.spring.sqs.demo.queue.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class SQSController {
    private final MessageProducer messageproducer;

    @GetMapping(value = "/send/{groupId}/{msg}")
    public @ResponseBody String sendSingleMessage(@PathVariable Integer groupId, @PathVariable String msg) {
        messageproducer.send(groupId, msg);
        return "Your message was sent.";
    }

    @GetMapping(value = "/send/{groupId}/batch/{count}")
    public @ResponseBody String sendSameGroupFlow(@PathVariable Integer groupId, @PathVariable Integer count) {
        sendFlow(groupId, count);
        return "All messages to group " + groupId + " were sent.";
    }

    private void sendFlow(int group, int count) {
        IntStream.rangeClosed(1, count).forEach(i -> messageproducer.send(group, "Message content " + i));
    }

}