package ru.itmo.pochtineploho.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.repositories.CatsRepository;
import ru.itmo.pochtineploho.repositories.OwnersRepository;
import ru.itmo.pochtineploho.models.Cat;
import ru.itmo.pochtineploho.models.CatModel;
import ru.itmo.pochtineploho.models.Owner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CatServiceImpl implements CatService {
    @Autowired
    public CatServiceImpl(CatsRepository catsRepository, OwnersRepository ownersRepository, CatFriendshipManager catFriendshipManager, CatModelMapper catModelMapper) {
        this.catsRepository = catsRepository;
        this.ownersRepository = ownersRepository;
        this.friendshipManager = catFriendshipManager;
        this.catMapper = catModelMapper;
    }

    private final CatsRepository catsRepository;
    private final OwnersRepository ownersRepository;
    private final CatFriendshipManager friendshipManager;
    private final CatModelMapper catMapper;

    @Override
    public CatModel saveCat(@NonNull CatModel catModel) {
        if (catModel.getId() != null && catsRepository.findById(catModel.getId()).isPresent()) {
            throw new NullPointerException("Cat " + catModel.getId() + " already exists");
        }
        ownersRepository.findById(catModel.getOwner()).orElseThrow(()
                -> new NullPointerException("There is no owner with id " + catModel.getId()));

        Cat cat = catMapper.modelToEntity(catModel);

        catsRepository.save(cat);

        for (Cat friend : cat.getFriends()) {
            friendshipManager.addFriendship(cat, friend);
        }

        return catMapper.entityToModel(cat);
    }

    @Override
    public CatModel updateCat(@NonNull CatModel catModel) {
        Cat existingCat = catsRepository.findById(catModel.getId()).orElseThrow(()
                -> new NullPointerException("Cat " + catModel.getId() + " does not exist"));

        Cat cat = catMapper.modelToEntity(catModel);

        ownersRepository.findById(catModel.getOwner()).orElseThrow(()
                -> new NullPointerException("There is no owner with id " + catModel.getOwner()));

        for (Cat friend : cat.getFriends()) {
            if (existingCat.getFriends().stream().noneMatch(c -> c.getId().equals(friend.getId()))) {
                friendshipManager.addFriendship(cat, friend);
            }
        }

        for (Cat friend : existingCat.getFriends()) {
            if (cat.getFriends().stream().noneMatch(c -> c.getId().equals(friend.getId()))) {
                friendshipManager.deleteFriendship(cat, friend);
            }
        }

        return catMapper.entityToModel(catsRepository.save(cat));
    }

    @Override
    public void deleteCat(@NonNull CatModel catModel) {
        Cat oldCat = catsRepository.findById(catModel.getId()).orElseThrow(
                () -> new NullPointerException("Cat " + catModel.getId() + " does not exist"));
        Owner owner = ownersRepository.findById(catModel.getOwner()).orElseThrow(
                () -> new NullPointerException("Owner " + catModel.getOwner() + " does not exist"));
        owner.getCats().removeIf(cat -> cat.getId().equals(catModel.getId()));
        List<Cat> oldCatFriends = oldCat.getFriends().stream().toList();
        for (Cat friend : oldCatFriends) {
            friendshipManager.deleteFriendship(oldCat, friend);
        }
        catsRepository.delete(catMapper.modelToEntity(catModel));
    }

    @Override
    public void deleteCatById(UUID id) {
        deleteCat(findCatById(id).orElseThrow(() -> new NullPointerException("Cat " + id + " does not exist")));
    }

    @Override
    public Optional<CatModel> findCatById(@NonNull UUID id) {
        return catsRepository.findById(id).map(catMapper::entityToModel);
    }

    @Override
    public List<CatModel> findCatsByName(String name) {
        return catsRepository.findByName(name).stream().map(catMapper::entityToModel).collect(Collectors.toList());
    }

    @Override
    public List<CatModel> findCatsByOwnerId(@NonNull UUID ownerId) {
        return catsRepository.findByOwnerId(ownerId).stream().map(catMapper::entityToModel).collect(Collectors.toList());
    }

    @Override
    public List<CatModel> getAllCats() {
        return catsRepository.findAll().stream().map(catMapper::entityToModel).collect(Collectors.toList());
    }
}
