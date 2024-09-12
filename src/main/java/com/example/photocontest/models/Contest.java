package com.example.photocontest.models;

import com.example.photocontest.models.enums.ContestPhase;
import com.example.photocontest.models.enums.ContestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "contests")
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    private String photoUrl;

    @ManyToMany
    @JoinTable(
            name = "contest_judges",
            joinColumns = @JoinColumn(name = "contest_id"),
            inverseJoinColumns = @JoinColumn(name = "judge_id"))
    private List<User> judges;

    @OneToOne
    @JoinColumn(name = "winner_id")
    private PhotoPost winner;

    @OneToMany(mappedBy = "contest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhotoPost> photoPosts;

    @Enumerated(EnumType.STRING)
    private ContestStatus status;

    @Enumerated(EnumType.STRING)
    private ContestPhase phase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;


    private LocalDateTime startDate;

    private LocalDateTime endDate;

    // New fields for phase start and end times
    private LocalDateTime phaseIStartTime;
    private LocalDateTime phaseIEndTime;

    private LocalDateTime phaseIIStartTime;
    private LocalDateTime phaseIIEndTime;

}