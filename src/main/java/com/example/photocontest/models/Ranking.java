package com.example.photocontest.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "ranking")
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int rating;

    @ManyToOne
    @JoinColumn(name = "photo_post_id")
    private PhotoPost photoPost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;




}
