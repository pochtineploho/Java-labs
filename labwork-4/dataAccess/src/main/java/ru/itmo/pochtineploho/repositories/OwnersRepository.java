package ru.itmo.pochtineploho.repositories;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.pochtineploho.models.Owner;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OwnersRepository extends JpaRepository<Owner, UUID> {
    Optional<Owner> findByLogin(@NonNull String login);
}
