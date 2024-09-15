package ru.itmo.pochtineploho.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.pochtineploho.models.Owner;
import ru.itmo.pochtineploho.security.models.SecurityUser;
import ru.itmo.pochtineploho.repositories.OwnersRepository;

@Service("daoUserDetailsService")
public class DaoUserDetailsService implements UserDetailsService {

    private final OwnersRepository ownersRepository;

    @Autowired
    public DaoUserDetailsService(OwnersRepository ownersRepository) {
        this.ownersRepository = ownersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Owner owner = ownersRepository.findByLogin(username).orElseThrow(
                () -> new UsernameNotFoundException(username));

        return SecurityUser.fromOwner(owner);
    }
}
