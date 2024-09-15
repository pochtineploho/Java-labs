package ru.itmo.pochtineploho.services;

import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.models.OwnerModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface OwnerService {
    OwnerModel saveOwner(OwnerModel ownerModel);
    OwnerModel updateOwner(OwnerModel ownerModel);
    void deleteOwner(OwnerModel ownerModel);
    void deleteOwnerById(UUID id);
    Optional<OwnerModel> findOwnerById(UUID id);
    Optional<OwnerModel> findOwnerByLogin(String login);
    List<OwnerModel> getAllOwners();
}
