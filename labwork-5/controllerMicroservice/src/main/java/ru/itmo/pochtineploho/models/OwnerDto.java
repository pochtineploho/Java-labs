package ru.itmo.pochtineploho.models;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder(access = AccessLevel.PUBLIC)
public record OwnerDto(
        UUID id,
        String name,
        String login,
        String password,
        Date dateOfBirth,
        UserStatus status,
        Role role,
        List<UUID> cats) {
}
