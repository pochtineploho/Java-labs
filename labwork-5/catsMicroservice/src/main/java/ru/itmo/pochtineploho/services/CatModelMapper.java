package ru.itmo.pochtineploho.services;

import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.models.Cat;
import ru.itmo.pochtineploho.models.CatModel;

@Service
public interface CatModelMapper {
    CatModel entityToModel(Cat cat);
    Cat modelToEntity(CatModel catModel);
}
