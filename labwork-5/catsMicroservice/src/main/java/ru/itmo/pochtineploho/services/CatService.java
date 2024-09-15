package ru.itmo.pochtineploho.services;

import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.models.CatModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface CatService {
    CatModel saveCat(CatModel catModel);
    CatModel updateCat(CatModel catModel);
    void deleteCat(CatModel catModel);
    void deleteCatById(UUID id);
    Optional<CatModel> findCatById(UUID id);
    List<CatModel> findCatsByName(String name);
    List<CatModel> findCatsByOwnerId(UUID ownerId);
    List<CatModel> getAllCats();
}
