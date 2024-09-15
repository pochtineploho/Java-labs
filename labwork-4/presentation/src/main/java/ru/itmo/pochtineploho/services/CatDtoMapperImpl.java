package ru.itmo.pochtineploho.services;

import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.models.CatDto;
import ru.itmo.pochtineploho.models.CatModel;

@Service
public class CatDtoMapperImpl implements CatDtoMapper {

    public CatDtoMapperImpl() {
    }

    @Override
    public CatModel dtoToModel(CatDto catDto) {
        return CatModel.builder()
                .id(catDto.id())
                .name(catDto.name())
                .color(catDto.color())
                .breed(catDto.breed())
                .owner(catDto.owner())
                .dateOfBirth(catDto.dateOfBirth())
                .friends(catDto.friends())
                .build();
    }

    @Override
    public CatDto modelToDto(CatModel catModel) {
        return CatDto.builder()
                .id(catModel.getId())
                .name(catModel.getName())
                .color(catModel.getColor())
                .owner(catModel.getOwner())
                .dateOfBirth(catModel.getDateOfBirth())
                .breed(catModel.getBreed())
                .friends(catModel.getFriends())
                .build();
    }
}
