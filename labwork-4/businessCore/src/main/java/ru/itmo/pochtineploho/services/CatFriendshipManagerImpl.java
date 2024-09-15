package ru.itmo.pochtineploho.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.repositories.CatsRepository;
import ru.itmo.pochtineploho.models.Cat;

@Service
public class CatFriendshipManagerImpl implements CatFriendshipManager {
    @Autowired
    public CatFriendshipManagerImpl(CatsRepository catsRepository) {
        this.catsRepository = catsRepository;
    }

    private final CatsRepository catsRepository;

    public void addFriendship(@NonNull Cat firstCat, @NonNull Cat secondCat) {
        if (firstCat.getFriends().stream().noneMatch(cat -> cat.getId().equals(secondCat.getId()))) {
            firstCat.getFriends().add(secondCat);
            catsRepository.save(firstCat);
        }
        if (secondCat.getFriends().stream().noneMatch(cat -> cat.getId().equals(firstCat.getId()))) {
            secondCat.getFriends().add(firstCat);
            catsRepository.save(secondCat);
        }
    }

    @Override
    public void deleteFriendship(@NonNull Cat firstCat, @NonNull Cat secondCat) {
        if (firstCat.getFriends().stream().anyMatch(cat -> cat.getId().equals(secondCat.getId()))) {
            firstCat.getFriends().removeIf(cat -> cat.getId() == secondCat.getId());
            catsRepository.save(firstCat);
        }
        if (secondCat.getFriends().stream().anyMatch(cat -> cat.getId().equals(firstCat.getId()))) {
            secondCat.getFriends().removeIf(cat -> cat.getId() == firstCat.getId());
            catsRepository.save(secondCat);
        }
    }
}
