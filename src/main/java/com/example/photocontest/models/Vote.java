package com.example.photocontest.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "photo_post_id")
    private PhotoPost photoPost;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean categoryMismatch;

    private int score;

    private String comment;

}