package ru.itmo.pochtineploho.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(name = "name", length = 32)
    private String name;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(unique = true, name = "login", nullable = false)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "status", columnDefinition = "user_status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "role", columnDefinition = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Cat> cats;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Owner owner = (Owner) o;
        return getId() != null && Objects.equals(getId(), owner.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}