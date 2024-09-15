package ru.itmo.pochtineploho.services;

import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.models.Owner;
import ru.itmo.pochtineploho.models.OwnerModel;

@Service
public interface OwnerModelMapper {
        OwnerModel entityToModel(Owner owner);
        Owner modelToEntity(OwnerModel ownerModel);
}
