package ru.itmo.pochtineploho.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.kafka.CatsKafkaProducer;
import ru.itmo.pochtineploho.models.CatModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ControllerCatServiceImpl implements ControllerCatService {

    final CatsKafkaProducer catsKafkaProducer;

    ObjectMapper mapper;

    public ControllerCatServiceImpl(CatsKafkaProducer catsKafkaProducer) {
        this.catsKafkaProducer = catsKafkaProducer;
        mapper = new ObjectMapper();
    }

    @Override
    public CatModel saveCat(@NonNull CatModel catModel) {
        try {
            return mapper.readValue(catsKafkaProducer.kafkaRequestReply("saveCat", mapper.writeValueAsString(catModel)).toString(), CatModel.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CatModel updateCat(@NonNull CatModel catModel) {
        try {
            return mapper.readValue(catsKafkaProducer.kafkaRequestReply("updateCat", mapper.writeValueAsString(catModel)).toString(), CatModel.class);
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCat(@NonNull CatModel catModel) {
        try {
            catsKafkaProducer.kafkaRequestReply("deleteCat", mapper.writeValueAsString(catModel));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCatById(UUID id) {
        try {
            catsKafkaProducer.kafkaRequestReply("deleteCatById", mapper.writeValueAsString(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CatModel> findCatById(@NonNull UUID id) {
        try {
            return Optional.of(mapper.readValue(catsKafkaProducer.kafkaRequestReply("findCatById", mapper.writeValueAsString(id)).toString(), CatModel.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CatModel> findCatsByName(String name) {
        try {
            return mapper.readValue(catsKafkaProducer.kafkaRequestReply("findCatsByName", "").toString(), new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CatModel> findCatsByOwnerId(@NonNull UUID ownerId) {
        try {
            return mapper.readValue(catsKafkaProducer.kafkaRequestReply("findCatsByOwnerId", "").toString(), new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CatModel> getAllCats() {
        try {
            return mapper.readValue(catsKafkaProducer.kafkaRequestReply("getAllCats", "").toString(), new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
