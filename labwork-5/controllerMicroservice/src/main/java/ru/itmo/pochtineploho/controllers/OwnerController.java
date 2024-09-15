package ru.itmo.pochtineploho.controllers;

import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import ru.itmo.pochtineploho.models.OwnerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OwnerController {
    ResponseEntity<OwnerDto> saveOwner(@NonNull OwnerDto ownerDto);

    ResponseEntity<Object> updateOwner(@NonNull OwnerDto ownerDto);

    void deleteOwner(@NonNull OwnerDto ownerDto);

    void deleteOwnerById(@NonNull UUID id);

    Optional<OwnerDto> findOwnerById(@NonNull UUID id);

    List<OwnerDto> getAllOwners();
}
