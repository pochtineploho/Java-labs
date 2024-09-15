package ru.itmo.pochtineploho.services;

import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.models.CatDto;
import ru.itmo.pochtineploho.models.CatModel;

@Service
public interface CatDtoMapper {
    CatModel dtoToModel(CatDto catDto);
    CatDto modelToDto(CatModel catModel);
}
