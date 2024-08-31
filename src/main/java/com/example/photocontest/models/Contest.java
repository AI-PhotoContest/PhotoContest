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

    @OneToMany
    private List<PhotoPost> photoPosts;

    @Enumerated(EnumType.STRING)
    private ContestStatus status;

    @Enumerated(EnumType.STRING)
    private ContestPhase phase;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;


    private LocalDate startDate;

    private LocalDate endDate;

    // New fields for phase start and end times
    private LocalDate phaseIStartTime;
    private LocalDate phaseIEndTime;

    private LocalDate phaseIIStartTime;
    private LocalDate phaseIIEndTime;

}