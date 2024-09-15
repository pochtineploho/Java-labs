package ru.itmo.pochtineploho.models;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder(access = AccessLevel.PUBLIC)
public record CatDto(
        UUID id,
        String name,
        Date dateOfBirth,
        String breed,
        CatColor color,
        UUID owner,
        List<UUID> friends) {
}
