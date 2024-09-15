package ru.itmo.pochtineploho.services;

import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.models.Cat;

@Service
public interface CatFriendshipManager {
    void addFriendship(Cat firstCat, Cat secondCat);

    void deleteFriendship(Cat firstCat, Cat secondCat);
}
