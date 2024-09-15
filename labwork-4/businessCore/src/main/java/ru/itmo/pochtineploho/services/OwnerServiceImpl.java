package ru.itmo.pochtineploho.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.repositories.CatsRepository;
import ru.itmo.pochtineploho.repositories.OwnersRepository;
import ru.itmo.pochtineploho.models.Cat;
import ru.itmo.pochtineploho.models.Owner;
import ru.itmo.pochtineploho.models.OwnerModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OwnerServiceImpl implements OwnerService {
    @Autowired
    public OwnerServiceImpl(OwnersRepository ownersRepository, CatsRepository catsRepository, OwnerModelMapper ownerModelMapper) {
        this.ownersRepository = ownersRepository;
        this.catsRepository = catsRepository;
        this.ownerMapper = ownerModelMapper;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
    }

    private final OwnersRepository ownersRepository;
    private final CatsRepository catsRepository;
    private final OwnerModelMapper ownerMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public OwnerModel saveOwner(@NonNull OwnerModel ownerModel) {
        if (ownerModel.getId() != null && ownersRepository.findById(ownerModel.getId()).isPresent()) {
            throw new NullPointerException("Owner " + ownerModel.getId() + " already exists");
        }
        ownerModel.setPassword(passwordEncoder.encode(ownerModel.getPassword()));
        return ownerMapper.entityToModel(ownersRepository.save(ownerMapper.modelToEntity(ownerModel)));
    }

    @Override
    public OwnerModel updateOwner(@NonNull OwnerModel ownerModel) {
        Owner owner = ownerMapper.modelToEntity(ownerModel);
        Owner existingOwner = ownersRepository.findById(ownerModel.getId()).orElseThrow(()
                -> new NullPointerException("Owner " + ownerModel.getId() + " does not exist"));

        if (!existingOwner.getPassword().equals(owner.getPassword())) {
            owner.setPassword(passwordEncoder.encode(ownerModel.getPassword()));
        }

        for (UUID catId : ownerModel.getCats()) {
            Optional<Cat> catOptional = catsRepository.findById(catId);
            if (catOptional.isEmpty()) {
                throw new NullPointerException("There is no cats with id " + catId);
            }

            catOptional.get().setOwner(owner);
            catsRepository.save(catOptional.get());
        }

        for (Cat cat : existingOwner.getCats()) {
            if (owner.getCats().stream().noneMatch(c -> c.getId().equals(cat.getId()))) {
                cat.setOwner(null);
                catsRepository.save(cat);
            }
        }
        return ownerMapper.entityToModel(ownersRepository.save(owner));
    }

    @Override
    public void deleteOwner(@NonNull OwnerModel ownerModel) {
        if (ownersRepository.findById(ownerModel.getId()).isEmpty()) {
            throw new NullPointerException("Owner " + ownerModel.getId() + " does not exist");
        }
        ownersRepository.delete(ownerMapper.modelToEntity(ownerModel));
        catsRepository.findByOwnerId(ownerModel.getId()).forEach(cat -> cat.setOwner(null));
    }

    @Override
    public void deleteOwnerById(@NonNull UUID id) {
        if (ownersRepository.findById(id).isEmpty()) {
            throw new NullPointerException("Owner " + id + " does not exist");
        }
        ownersRepository.deleteById(id);
        catsRepository.findByOwnerId(id).forEach(cat -> cat.setOwner(null));
    }

    @Override
    public Optional<OwnerModel> findOwnerById(@NonNull UUID id) {
        return ownersRepository.findById(id).map(ownerMapper::entityToModel);
    }

    @Override
    public Optional<OwnerModel> findOwnerByLogin(@NonNull String login) {
        return ownersRepository.findByLogin(login).map(ownerMapper::entityToModel);
    }

    @Override
    public List<OwnerModel> getAllOwners() {
        return ownersRepository.findAll().stream().map(ownerMapper::entityToModel).collect(Collectors.toList());
    }
}
