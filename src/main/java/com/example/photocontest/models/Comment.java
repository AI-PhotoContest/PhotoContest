package com.example.photocontest.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comments")
public class Comment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private String content;
        private String date;
        @ManyToOne
        @JoinColumn(name = "creator_id")
        private User creator;

        @ManyToOne
        @JoinColumn(name = "photo_post_id")
        private PhotoPost photoPost;
}
