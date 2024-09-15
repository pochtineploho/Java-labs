package ru.itmo.pochtineploho.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OwnersKafkaProducer {
    public ReplyingKafkaTemplate<String, Object, Object> ownerReplyingTemplate;

    @Autowired
    public OwnersKafkaProducer(@Qualifier("replyingOwnersTemplate") ReplyingKafkaTemplate<String, Object, Object> ownerReplyingTemplate) {
        this.ownerReplyingTemplate = ownerReplyingTemplate;
    }

    @Autowired
    public void setCatReplyingTemplate(@Qualifier("replyingCatsTemplate") ReplyingKafkaTemplate<String, Object, Object> ownerReplyingTemplate) {
        this.ownerReplyingTemplate = ownerReplyingTemplate;
    }

    public Object kafkaRequestReply(String key, Object request) throws Exception {
        ProducerRecord<String, Object> record = new ProducerRecord<>("owners.send", key, request);
        RequestReplyFuture<String, Object, Object> replyFuture = ownerReplyingTemplate.sendAndReceive(record);
        replyFuture.getSendFuture().get(100, TimeUnit.SECONDS);

        return replyFuture.get(100, TimeUnit.SECONDS).value();
    }
}
