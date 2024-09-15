package ru.itmo.pochtineploho.services;

import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.models.OwnerDto;
import ru.itmo.pochtineploho.models.OwnerModel;

@Service
public class OwnerDtoMapperImpl implements OwnerDtoMapper {

    public OwnerDtoMapperImpl() {
    }

    @Override
    public OwnerModel dtoToModel(OwnerDto ownerDto) {
        return OwnerModel.builder()
                .id(ownerDto.id())
                .name(ownerDto.name())
                .login(ownerDto.login())
                .password(ownerDto.password())
                .dateOfBirth(ownerDto.dateOfBirth())
                .role(ownerDto.role())
                .status(ownerDto.status())
                .cats(ownerDto.cats())
                .build();
    }

    @Override
    public OwnerDto modelToDto(OwnerModel ownerModel) {
        return OwnerDto.builder()
                .id(ownerModel.getId())
                .name(ownerModel.getName())
                .login(ownerModel.getLogin())
                .password(ownerModel.getPassword())
                .dateOfBirth(ownerModel.getDateOfBirth())
                .status(ownerModel.getStatus())
                .role(ownerModel.getRole())
                .cats(ownerModel.getCats())
                .build();
    }
}
