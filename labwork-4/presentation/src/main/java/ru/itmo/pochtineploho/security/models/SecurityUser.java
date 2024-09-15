package ru.itmo.pochtineploho.security.models;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ru.itmo.pochtineploho.models.Owner;
import ru.itmo.pochtineploho.models.UserStatus;

import java.util.Collection;
import java.util.List;

@Data
public class SecurityUser implements UserDetails {
    private String login;
    private String password;
    private UserStatus status;
    private List<? extends GrantedAuthority> authorities;

    public static UserDetails fromOwner(Owner owner) {
        return new User(
                owner.getLogin(),
                owner.getPassword(),
                owner.getStatus().equals(UserStatus.ACTIVE),
                !owner.getStatus().equals(UserStatus.UNACTIVE),
                !owner.getStatus().equals(UserStatus.UNACTIVE),
                !owner.getStatus().equals(UserStatus.BANNED),
                SecurityRole.roleOf(owner.getRole()).getAuthorities()
                );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !status.equals(UserStatus.UNACTIVE);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !status.equals(UserStatus.BANNED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !status.equals(UserStatus.UNACTIVE);
    }

    @Override
    public boolean isEnabled() {
        return status.equals(UserStatus.ACTIVE);
    }
}
