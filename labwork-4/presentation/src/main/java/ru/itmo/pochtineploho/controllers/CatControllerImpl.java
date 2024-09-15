package ru.itmo.pochtineploho.controllers;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.itmo.pochtineploho.models.CatDto;
import ru.itmo.pochtineploho.services.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cats")
public class CatControllerImpl implements CatController {
    private final CatService catService;
    private final CatDtoMapper catMapper;
    private final OwnerService ownerService;

    @Autowired
    public CatControllerImpl(CatService catService, CatDtoMapper catDtoMapper, OwnerService ownerService) {
        this.catMapper = catDtoMapper;
        this.catService = catService;
        this.ownerService = ownerService;
    }

    @Override
    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('cat:create_all', 'cat:create')")
    public ResponseEntity<CatDto> saveCat(@NonNull @RequestBody CatDto catDto) {
        boolean hasSuperAccess = getCurrentUser().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("cat:create_all"));

        if (hasSuperAccess || catDto.owner().equals(getCurrentUserId())) {
            try {
                CatDto cat = catMapper.modelToDto(catService.saveCat(catMapper.dtoToModel(catDto)));
                return ResponseEntity.status(HttpStatus.CREATED).body(cat);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @Override
    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('cat:update_all', 'cat:update')")
    public ResponseEntity<CatDto> updateCat(@NonNull @RequestBody CatDto catDto) {
        if (findCatById(catDto.id()).isPresent()) {
            try {
                CatDto cat = catMapper.modelToDto(catService.updateCat(catMapper.dtoToModel(catDto)));
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(cat);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @Override
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('cat:delete_all', 'cat:delete')")
    public ResponseEntity<Object> deleteCat(@NonNull @RequestBody CatDto catDto) {
        if (findCatById(catDto.id()).isPresent()) {
            try {
                catService.deleteCat(catMapper.dtoToModel(catDto));
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('cat:delete_all', 'cat:delete')")
    public ResponseEntity<Object> deleteCatById(@NonNull @PathVariable("id") UUID id) {
        if (findCatById(id).isPresent()) {
            try {
                catService.deleteCatById(id);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @Override
    @GetMapping("/find/{id}")
    @PreAuthorize("hasAnyAuthority('cat:read', 'cat:read_all')")
    public Optional<CatDto> findCatById(@NonNull @PathVariable("id") UUID id) {
        boolean hasSuperAccess = getCurrentUser().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("cat:read_all"));

        if (hasSuperAccess) {
            return catService.findCatById(id).map(catMapper::modelToDto);
        }

        return catService.findCatById(id)
                .filter(cat -> cat.getOwner().equals(getCurrentUserId()))
                .map(catMapper::modelToDto);
    }

    @Override
    @GetMapping("/findByOwner/{ownerId}")
    @PreAuthorize("hasAnyAuthority('cat:read_all')")
    public List<CatDto> findCatsByOwnerId(@NonNull @PathVariable("ownerId") UUID ownerId) {
        return catService.findCatsByOwnerId(ownerId).stream()
                .map(catMapper::modelToDto).collect(Collectors.toList());
    }

    @Override
    @GetMapping("/findByName/{name}")
    @PreAuthorize("hasAnyAuthority('cat:read', 'cat:read_all')")
    public List<CatDto> findCatsByName(@NonNull @PathVariable("name") String name) {
        boolean hasSuperAccess = getCurrentUser().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("cat:read_all"));

        if (hasSuperAccess) {
            return catService.findCatsByName(name).stream()
                    .map(catMapper::modelToDto).collect(Collectors.toList());
        }

        return catService.findCatsByName(name).stream()
                .filter(cat -> cat.getOwner().equals(getCurrentUserId()))
                .map(catMapper::modelToDto).collect(Collectors.toList());
    }

    @Override
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('cat:read', 'cat:read_all')")
    public List<CatDto> getAllCats() {
        boolean hasSuperAccess = getCurrentUser().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals("cat:read_all"));

        if (hasSuperAccess) {
            return catService.getAllCats().stream()
                    .map(catMapper::modelToDto).collect(Collectors.toList());
        }

        return catService.getAllCats().stream()
                .filter(cat -> cat.getOwner().equals(getCurrentUserId()))
                .map(catMapper::modelToDto).collect(Collectors.toList());
    }

    private UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UserDetails) authentication.getPrincipal());
    }


    private UUID getCurrentUserId() {
        String login = getCurrentUser().getUsername();
        return ownerService.findOwnerByLogin(login)
                .orElseThrow(() -> new NullPointerException("Owner " + login + " not found")).getId();
    }
}
