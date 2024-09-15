package ru.itmo.pochtineploho.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.itmo.pochtineploho.models.Cat;

import java.util.List;
import java.util.UUID;

@Repository
public interface CatsRepository extends JpaRepository<Cat, UUID> {

    @Query("SELECT c FROM Cat c WHERE c.owner.id = :id")
    List<Cat> findByOwnerId(@Param("id") UUID id);

    @Query("SELECT c FROM Cat c WHERE c.name = :name")
    List<Cat> findByName(@Param("name") String name);
}
