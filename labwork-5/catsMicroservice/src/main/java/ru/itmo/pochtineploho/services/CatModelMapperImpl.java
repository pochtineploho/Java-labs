package ru.itmo.pochtineploho.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.repositories.CatsRepository;
import ru.itmo.pochtineploho.repositories.OwnersRepository;
import ru.itmo.pochtineploho.models.Cat;
import ru.itmo.pochtineploho.models.CatModel;
import ru.itmo.pochtineploho.models.Owner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CatModelMapperImpl implements CatModelMapper {
    @Autowired
    public CatModelMapperImpl(OwnersRepository ownersRepository, CatsRepository catsRepository) {
        this.ownersRepository = ownersRepository;
        this.catsRepository = catsRepository;
    }

    private final OwnersRepository ownersRepository;
    private final CatsRepository catsRepository;

    @Override
    public CatModel entityToModel(Cat cat) {
        List<UUID> friendsId = new ArrayList<>();
        for (Cat friend : cat.getFriends()) {
            friendsId.add(friend.getId());
        }

        return CatModel.builder()
                .id(cat.getId())
                .name(cat.getName())
                .breed(cat.getBreed())
                .dateOfBirth(cat.getDateOfBirth())
                .color(cat.getColor())
                .owner(cat.getOwner().getId())
                .friends(friendsId)
                .build();
    }

    @Override
    public Cat modelToEntity(CatModel catModel) {
        List<Cat> friends = new ArrayList<>();
        for (UUID friendId : catModel.getFriends()) {
            Optional<Cat> friend = catsRepository.findById(friendId);
            friend.ifPresent(friends::add);
        }
        Optional<Owner> ownerOptional = ownersRepository.findById(catModel.getOwner());
        if (ownerOptional.isEmpty()) {
            throw new NullPointerException("There is no owner with id " + catModel.getOwner());
        }

        return Cat.builder()
                .id(catModel.getId())
                .name(catModel.getName())
                .breed(catModel.getBreed())
                .dateOfBirth(catModel.getDateOfBirth())
                .color(catModel.getColor())
                .owner(ownerOptional.get())
                .friends(friends)
                .build();
    }
}
