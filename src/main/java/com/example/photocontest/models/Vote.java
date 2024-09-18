package com.example.photocontest.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
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
    private User judge;

    private boolean categoryMismatch;

    //This is made, so we can have a default score if no one judges this photo post
    private int score = 3;

    private String comment;

}