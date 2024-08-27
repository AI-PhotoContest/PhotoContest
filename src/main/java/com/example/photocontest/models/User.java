package com.example.photocontest.models;

import com.example.photocontest.models.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "role_id")
    private Role role;

    private String firstName;

    private String lastName;

    private String profilePicture;

    private int points;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Column(name = "is_banned")
    private boolean isBanned;

    @Column(name = "is_votable")
    private boolean isVotable;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toString()));
    }


}
