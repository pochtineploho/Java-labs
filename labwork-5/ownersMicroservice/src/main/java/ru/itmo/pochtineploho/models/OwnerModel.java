package ru.itmo.pochtineploho.models;

import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OwnerModel {
    private UUID id;
    private String name;
    private String login;
    private String password;
    private Date dateOfBirth;
    private UserStatus status;
    private Role role;
    private List<UUID> cats;
}
