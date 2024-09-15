package ru.itmo.pochtineploho.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.itmo.pochtineploho.models.OwnerModel;
import ru.itmo.pochtineploho.services.OwnerService;

import java.util.UUID;

@Component
public class OwnersKafkaConsumer {
    ObjectMapper mapper;
    OwnerService ownerService;

    @Autowired
    public OwnersKafkaConsumer(OwnerService ownerService) {
        mapper = new ObjectMapper();
        this.ownerService = ownerService;
    }

    @SneakyThrows
    @KafkaListener(groupId = "owners", topics = "owners.send")
    @SendTo
    public Message<?> listen(ConsumerRecord<String, Object> consumerRecord) {
        String key = consumerRecord.key();
        String record = mapper.readTree(consumerRecord.value().toString()).toString();

        String response = "";
        switch (key) {
            case "saveOwner" -> ownerService.saveOwner(mapper.readValue(record, OwnerModel.class));
            case "updateOwner" -> ownerService.updateOwner(mapper.readValue(record, OwnerModel.class));
            case "deleteOwner" -> ownerService.deleteOwner(mapper.readValue(record, OwnerModel.class));
            case "deleteOwnerById" -> ownerService.deleteOwnerById(mapper.readValue(record, UUID.class));
            case "findOwnerByID" ->
                    response = mapper.writeValueAsString(ownerService.findOwnerById(mapper.readValue(record, UUID.class)).orElseThrow());
            case "findOwnerByLogin" ->
                    response = mapper.writeValueAsString(ownerService.findOwnerByLogin(mapper.readValue(record, String.class)).orElseThrow());
            case "getAllOwners" -> response = mapper.writeValueAsString(ownerService.getAllOwners());
        }
        return MessageBuilder.withPayload(response).setHeader(KafkaHeaders.KEY, key).build();
    }
}
