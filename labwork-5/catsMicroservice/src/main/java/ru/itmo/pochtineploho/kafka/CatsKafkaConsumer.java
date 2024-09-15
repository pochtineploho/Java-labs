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
import ru.itmo.pochtineploho.models.CatModel;
import ru.itmo.pochtineploho.services.CatService;

import java.util.UUID;

@Component
public class CatsKafkaConsumer {
    ObjectMapper mapper;
    CatService catService;

    @Autowired
    public CatsKafkaConsumer(CatService catService) {
        mapper = new ObjectMapper();
        this.catService = catService;
    }

    @SneakyThrows
    @KafkaListener(groupId = "cats", topics = "cats.send")
    @SendTo
    public Message<?> listen(ConsumerRecord<String, Object> consumerRecord) {
        String key = consumerRecord.key();
        String record = mapper.readTree(consumerRecord.value().toString()).toString();

        String response = "";
        switch (key) {
            case "saveCat" -> catService.saveCat(mapper.readValue(record, CatModel.class));
            case "updateCat" -> catService.updateCat(mapper.readValue(record, CatModel.class));
            case "deleteCat" -> catService.deleteCat(mapper.readValue(record, CatModel.class));
            case "deleteCatById" -> catService.deleteCatById(mapper.readValue(record, UUID.class));
            case "findCatByID" ->
                    response = mapper.writeValueAsString(catService.findCatById(mapper.readValue(record, UUID.class)).orElseThrow());
            case "findCatsByName" ->
                    response = mapper.writeValueAsString(catService.findCatsByName(mapper.readValue(record, String.class)));
            case "findCatsByOwnerId" ->
                    response = mapper.writeValueAsString(catService.findCatsByOwnerId(mapper.readValue(record, UUID.class)));
            case "getAllCats" -> response = mapper.writeValueAsString(catService.getAllCats());
        }
        return MessageBuilder.withPayload(response).setHeader(KafkaHeaders.KEY, key).build();
    }
}
