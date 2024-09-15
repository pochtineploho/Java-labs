package ru.itmo.pochtineploho.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.kafka.OwnersKafkaProducer;
import ru.itmo.pochtineploho.models.OwnerModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ControllerOwnerServiceImpl implements ControllerOwnerService {
    final OwnersKafkaProducer ownersKafkaProducer;

    ObjectMapper mapper;

    public ControllerOwnerServiceImpl(OwnersKafkaProducer ownersKafkaProducer) {
        mapper = new ObjectMapper();
        this.ownersKafkaProducer = ownersKafkaProducer;
    }

    @Override
    public OwnerModel saveOwner(@NonNull OwnerModel ownerModel) {
        try {
            return mapper.readValue(ownersKafkaProducer.kafkaRequestReply("saveOwner", mapper.writeValueAsString(ownerModel)).toString(), OwnerModel.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OwnerModel updateOwner(@NonNull OwnerModel ownerModel) {
        try {
            return mapper.readValue(ownersKafkaProducer.kafkaRequestReply("updateOwner", mapper.writeValueAsString(ownerModel)).toString(), OwnerModel.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteOwner(OwnerModel ownerModel) {
        try {
            ownersKafkaProducer.kafkaRequestReply("deleteOwner", mapper.writeValueAsString(ownerModel));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteOwnerById(@NonNull UUID id) {
        try {
            ownersKafkaProducer.kafkaRequestReply("deleteOwnerById", mapper.writeValueAsString(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<OwnerModel> findOwnerById(@NonNull UUID id) {
        try {
            return Optional.of(mapper.readValue(ownersKafkaProducer.kafkaRequestReply("findOwnerById", mapper.writeValueAsString(id)).toString(), OwnerModel.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<OwnerModel> findOwnerByLogin(@NonNull String login) {
        try {
            return Optional.of(mapper.readValue(ownersKafkaProducer.kafkaRequestReply("findOwnerByLogin", mapper.writeValueAsString(login)).toString(), OwnerModel.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OwnerModel> getAllOwners() {
        try {
            return mapper.readValue(ownersKafkaProducer.kafkaRequestReply("getAllOwners", "").toString(), new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
