package ru.itmo.pochtineploho.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.repositories.CatsRepository;
import ru.itmo.pochtineploho.models.Cat;
import ru.itmo.pochtineploho.models.Owner;
import ru.itmo.pochtineploho.models.OwnerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OwnerModelMapperImpl implements OwnerModelMapper {
    @Autowired
    public OwnerModelMapperImpl(CatsRepository catsRepository) {
        this.catsRepository = catsRepository;
    }

    private final CatsRepository catsRepository;

    @Override
    public OwnerModel entityToModel(Owner owner) {
        List<UUID> catsId = new ArrayList<>();

        for (Cat cat : owner.getCats()) {
            catsId.add(cat.getId());
        }

        return OwnerModel.builder()
                .id(owner.getId())
                .name(owner.getName())
                .login(owner.getLogin())
                .password(owner.getPassword())
                .dateOfBirth(owner.getDateOfBirth())
                .status(owner.getStatus())
                .role(owner.getRole())
                .cats(catsId)
                .build();
    }

    @Override
    public Owner modelToEntity(OwnerModel ownerModel) {
        List<Cat> cats = new ArrayList<>();
        for (UUID catId : ownerModel.getCats()) {
            Optional<Cat> cat = catsRepository.findById(catId);
            cat.ifPresent(cats::add);
        }

        return Owner.builder()
                .id(ownerModel.getId())
                .name(ownerModel.getName())
                .login(ownerModel.getLogin())
                .password(ownerModel.getPassword())
                .dateOfBirth(ownerModel.getDateOfBirth())
                .status(ownerModel.getStatus())
                .role(ownerModel.getRole())
                .cats(cats)
                .build();
    }
}
