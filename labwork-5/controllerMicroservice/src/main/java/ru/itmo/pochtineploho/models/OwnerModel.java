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
