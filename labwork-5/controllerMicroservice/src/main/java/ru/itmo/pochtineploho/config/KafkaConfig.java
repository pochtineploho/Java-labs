package ru.itmo.pochtineploho.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.time.Duration;

@Configuration
public class KafkaConfig {
    @Bean
    public ConcurrentMessageListenerContainer<String, Object> replyCatsContainer(
            ConcurrentKafkaListenerContainerFactory<String, Object> containerFactory) {
        ConcurrentMessageListenerContainer<String, Object> repliesContainer = containerFactory.createContainer("cats.reply");
        repliesContainer.getContainerProperties().setGroupId("cats");
        repliesContainer.setAutoStartup(false);

        return repliesContainer;
    }

    @Bean
    public ReplyingKafkaTemplate<String, Object, Object> replyingCatsTemplate(
            ProducerFactory<String, Object> producerFactory,
            @Qualifier("replyCatsContainer") ConcurrentMessageListenerContainer<String, Object> replyCatsContainer) {
        ReplyingKafkaTemplate<String, Object, Object> replyTemplate = new ReplyingKafkaTemplate<>(producerFactory, replyCatsContainer);
        replyTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10));
        replyTemplate.setSharedReplyTopic(true);

        return replyTemplate;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, Object> replyOwnersContainer(
            ConcurrentKafkaListenerContainerFactory<String, Object> containerFactory) {
        ConcurrentMessageListenerContainer<String, Object> repliesContainer = containerFactory.createContainer("owners.reply");
        repliesContainer.getContainerProperties().setGroupId("owners");
        repliesContainer.setAutoStartup(false);

        return repliesContainer;
    }

    @Bean
    public ReplyingKafkaTemplate<String, Object, Object> replyingOwnersTemplate(
            ProducerFactory<String, Object> producerFactory,
            @Qualifier("replyOwnersContainer") ConcurrentMessageListenerContainer<String, Object> replyOwnersContainer) {
        ReplyingKafkaTemplate<String, Object, Object> replyTemplate = new ReplyingKafkaTemplate<>(producerFactory, replyOwnersContainer);
        replyTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10));
        replyTemplate.setSharedReplyTopic(true);

        return replyTemplate;
    }
}