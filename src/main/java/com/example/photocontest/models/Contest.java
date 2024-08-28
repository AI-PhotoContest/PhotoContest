package com.example.photocontest.models;


import com.example.photocontest.models.enums.ContestPhase;
import com.example.photocontest.models.enums.ContestStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;

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

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

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
}
