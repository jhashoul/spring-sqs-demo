package com.jh.spring.sqs.demo.services;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@NoArgsConstructor
public class DemoService {

    private static AtomicInteger THREAD_NUM = new AtomicInteger(1);
    private static ThreadLocal<Integer> THREADS = ThreadLocal.withInitial(() -> THREAD_NUM.getAndIncrement());
    private static Logger logger = LoggerFactory.getLogger(DemoService.class);
    private static Random random = new Random();

    @SneakyThrows
    /**
     * Simulate long processing
     */
    public void process(String payload, String groupId) {
        logger.info("Received message from consumer {}, payload {}, group {}", THREADS.get(), payload, groupId);
        Thread.sleep(random.nextLong(1000));
    }
}
