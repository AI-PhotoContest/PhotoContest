package com.example.photocontest.models.dto;

import com.example.photocontest.models.enums.ContestPhase;
import com.example.photocontest.models.enums.ContestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ContestDto {

    @NotBlank(message = "Title is mandatory")
    private String title;

    private String description;

    private String photoUrl;

    @NotNull(message = "Start date is mandatory")
    private LocalDateTime startDate;

    @NotNull(message = "End date is mandatory")
    private LocalDateTime endDate;

    @NotNull(message = "Status is mandatory")
    private ContestStatus status;

    @NotNull(message = "Category is mandatory")
    private String category;

    @NotNull(message = "Phase I start time is required")
    private LocalDateTime phaseIStartTime;

    @NotNull(message = "Phase I end time is required")
    private LocalDateTime phaseIEndTime;

    @NotNull(message = "Phase II start time is required")
    private LocalDateTime phaseIIStartTime;

    @NotNull(message = "Phase II end time is required")
    private LocalDateTime phaseIIEndTime;
}