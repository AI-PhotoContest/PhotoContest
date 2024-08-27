package com.example.photocontest.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "photo_posts")
public class PhotoPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String title;
    private String description;
    private String image;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ranking_id")
    private Ranking ranking;

    @ManyToOne
    @JoinColumn(name = "likes_id")
    private User likes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_post_id")
    private Set<Comment> comments;

    @Column(name = "seen_count")
    private int seenCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "camera_details_id")
    private CameraDetails cameraDetails;

    @Temporal(TemporalType.DATE)
    @Column(name = "upload_date")
    private Date uploadDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "taken_date")
    private Date takenDate;

    @Column(name = "retouching_applied")
    private String retouchingApplied;
    private String location;
}
