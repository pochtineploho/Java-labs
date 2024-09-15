package ru.itmo.pochtineploho.services;

import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.models.OwnerDto;
import ru.itmo.pochtineploho.models.OwnerModel;

@Service
public interface OwnerDtoMapper {
        OwnerModel dtoToModel(OwnerDto ownerDto);
        OwnerDto modelToDto(OwnerModel ownerModel);
}
