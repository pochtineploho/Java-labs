package ru.itmo.pochtineploho.models;

import lombok.*;

import java.util.*;

@Data
@Builder
public class CatModel {
    private UUID id;
    private String name;
    private Date dateOfBirth;
    private String breed;
    private CatColor color;
    private UUID owner;
    private List<UUID> friends;
}
