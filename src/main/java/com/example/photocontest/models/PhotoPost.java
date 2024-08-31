package com.example.photocontest.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "photo_posts")
public class PhotoPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    private String image;

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

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinTable(
            name = "photo_post_tags",
            joinColumns = @JoinColumn(name = "photo_post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @OneToMany(mappedBy = "photoPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vote> votes;

    public int getScore() {
        return votes.stream().mapToInt(Vote::getScore).sum();
    }
}
