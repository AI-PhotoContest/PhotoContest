package com.example.photocontest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    private String firstName;

    private String lastName;

    private String profilePicture;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "points_id")
    private PointsSystem points;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Column(name = "is_banned")
    private boolean isBanned;

    @Column(name = "is_votable")
    private boolean isVotable;



}
