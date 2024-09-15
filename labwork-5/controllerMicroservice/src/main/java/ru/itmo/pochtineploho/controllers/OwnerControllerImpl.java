package ru.itmo.pochtineploho.controllers;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itmo.pochtineploho.models.OwnerDto;
import ru.itmo.pochtineploho.services.OwnerDtoMapper;
import ru.itmo.pochtineploho.services.ControllerOwnerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/owners")
public class OwnerControllerImpl implements OwnerController {
    private final ControllerOwnerService controllerOwnerService;
    private final OwnerDtoMapper ownerMapper;

    @Autowired
    public OwnerControllerImpl(ControllerOwnerService controllerOwnerService, OwnerDtoMapper ownerDtoMapper) {
        this.ownerMapper = ownerDtoMapper;
        this.controllerOwnerService = controllerOwnerService;
    }

    @Override
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('owner:create')")
    public ResponseEntity<OwnerDto> saveOwner(@NonNull @RequestBody OwnerDto ownerDto) {
        try {
            OwnerDto owner = ownerMapper.modelToDto(controllerOwnerService.saveOwner(ownerMapper.dtoToModel(ownerDto)));
            return ResponseEntity.status(HttpStatus.CREATED).body(owner);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('owner:update')")
    public ResponseEntity<Object> updateOwner(@NonNull @RequestBody OwnerDto ownerDto) {
        try {
            OwnerDto owner =  ownerMapper.modelToDto(controllerOwnerService.updateOwner(ownerMapper.dtoToModel(ownerDto)));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(owner);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('owner:delete')")
    public void deleteOwner(@NonNull @RequestBody OwnerDto ownerDto) {
        controllerOwnerService.deleteOwner(ownerMapper.dtoToModel(ownerDto));
    }

    @Override
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('owner:delete')")
    public void deleteOwnerById(@NonNull @PathVariable("id") UUID id) {
        controllerOwnerService.deleteOwnerById(id);
    }

    @Override
    @GetMapping("/find/{id}")
    @PreAuthorize("hasAuthority('owner:read')")
    public Optional<OwnerDto> findOwnerById(@NonNull @PathVariable("id") UUID id) {
        return controllerOwnerService.findOwnerById(id).map(ownerMapper::modelToDto);
    }

    @Override
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('owner:read')")
    public List<OwnerDto> getAllOwners() {
        return controllerOwnerService.getAllOwners().stream().map(ownerMapper::modelToDto).collect(Collectors.toList());
    }
}
