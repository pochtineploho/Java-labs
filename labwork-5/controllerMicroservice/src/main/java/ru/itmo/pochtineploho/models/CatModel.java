package ru.itmo.pochtineploho.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
