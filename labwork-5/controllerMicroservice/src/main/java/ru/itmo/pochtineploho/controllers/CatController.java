package ru.itmo.pochtineploho.controllers;

import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import ru.itmo.pochtineploho.models.CatDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CatController {
    ResponseEntity<CatDto> saveCat(@NonNull CatDto catDto);

    ResponseEntity<CatDto> updateCat(@NonNull CatDto catDto);

    ResponseEntity<Object> deleteCat(@NonNull CatDto catDto);

    ResponseEntity<Object> deleteCatById(@NonNull UUID id);

    Optional<CatDto> findCatById(@NonNull UUID id);

    List<CatDto> findCatsByOwnerId(@NonNull UUID ownerId);

    List<CatDto> findCatsByName(@NonNull String name);

    List<CatDto> getAllCats();
}
