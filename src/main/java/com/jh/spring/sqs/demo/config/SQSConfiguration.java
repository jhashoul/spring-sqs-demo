package com.jh.spring.sqs.demo.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.jh.spring.sqs.demo.queue.MessageConsumerOrderGuarantor;
import com.jh.spring.sqs.demo.queue.MessageProducer;
import com.jh.spring.sqs.demo.services.DemoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SQSConfiguration {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.credentials.access-key}")
    private String awsAccessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String awsSecretKey;

    @Value("${queue.demo-service.url}")
    private String queueName;

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean
    public MessageProducer messageProducer(QueueMessagingTemplate queueMessagingTemplate) {
        return new MessageProducer(queueName, queueMessagingTemplate);
    }

    @Bean
    public MessageConsumerOrderGuarantor messageConsumerOrderGuarantor(DemoService demoService, AmazonSQSAsync amazonSQSAsync) {
        return new MessageConsumerOrderGuarantor(queueName, demoService, amazonSQSAsync);
    }

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .build();
    }

}