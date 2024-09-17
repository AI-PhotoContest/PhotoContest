package com.example.photocontest.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "camera_details_id")
    private CameraDetails cameraDetails;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @Column(name = "taken_date")
    private LocalDate takenDate;

    @Column(name = "retouching_applied")
    private String retouchingApplied;

    private String location;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinTable(
            name = "photo_post_tags",
            joinColumns = @JoinColumn(name = "photo_post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    private Contest contest;

    @OneToMany(mappedBy = "photoPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vote> votes;

    public int getScore() {
        return votes.stream().mapToInt(Vote::getScore).sum();
    }

    public void addVote(Vote vote) {
        if (votes == null) {
            votes = List.of();
        }
        votes.add(vote);
    }

    // Method to calculate average score or set default score
    public double getAverageScore() {
        if (votes == null || votes.isEmpty()) {
            return 3; // Default score if no ratings
        }
        return votes.stream()
                .mapToInt(Vote::getScore)
                .average()
                .orElse(3);
    }
}
