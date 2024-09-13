package com.example.photocontest.mappers;

import com.example.photocontest.models.Category;
import com.example.photocontest.models.Contest;
import com.example.photocontest.models.User;
import com.example.photocontest.models.dto.ContestDto;
import com.example.photocontest.models.enums.ContestPhase;
import com.example.photocontest.repositories.CategoryRepository;
import com.example.photocontest.services.contracts.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ContestMapper {

    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    @Autowired
    public ContestMapper(CategoryRepository categoryRepository, CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    public Contest fromDto(ContestDto contestDto, User creator) {
        validatePhaseTimings(contestDto);

        Contest contest = new Contest();
        contest.setTitle(contestDto.getTitle());
        contest.setDescription(contestDto.getDescription());
        contest.setPhotoUrl(contestDto.getPhotoUrl());

        contest.setCreator(creator);

        contest.setStartDate(contestDto.getStartDate());
        contest.setEndDate(contestDto.getEndDate());
        contest.setStatus(contestDto.getStatus());
        contest.setPhase(ContestPhase.PHASE1);

        contest.setPhaseIStartTime(contestDto.getPhaseIStartTime());
        contest.setPhaseIEndTime(contestDto.getPhaseIEndTime());
        contest.setPhaseIIStartTime(contestDto.getPhaseIIStartTime());
        contest.setPhaseIIEndTime(contestDto.getPhaseIIEndTime());

        String cleanedCategory = contestDto.getCategory().replace(",", "").trim();

        Category category = categoryRepository.findByName(cleanedCategory);
        if (category == null) {
            categoryService.createCategory(contestDto.getCategory());
            category = categoryRepository.findByName(cleanedCategory);
        }

        contest.setCategory(category);

        return contest;
    }

    public ContestDto toDto(Contest contest) {
        ContestDto contestDto = new ContestDto();
        contestDto.setTitle(contest.getTitle());
        contestDto.setDescription(contest.getDescription());
        contestDto.setPhotoUrl(contest.getPhotoUrl());

        contestDto.setStartDate(contest.getStartDate());
        contestDto.setEndDate(contest.getEndDate());
        contestDto.setStatus(contest.getStatus());

        contestDto.setPhaseIStartTime(contest.getPhaseIStartTime());
        contestDto.setPhaseIEndTime(contest.getPhaseIEndTime());
        contestDto.setPhaseIIStartTime(contest.getPhaseIIStartTime());
        contestDto.setPhaseIIEndTime(contest.getPhaseIIEndTime());

        contestDto.setCategory(contest.getCategory().getName());

        return contestDto;
    }

    public Contest updateContestFromDto(Contest contest, ContestDto contestDto) {
        validatePhaseTimings(contestDto);

        contest.setTitle(contestDto.getTitle());
        contest.setDescription(contestDto.getDescription());
        contest.setPhotoUrl(contestDto.getPhotoUrl());

        contest.setStartDate(contestDto.getStartDate());
        contest.setEndDate(contestDto.getEndDate());
        contest.setStatus(contestDto.getStatus());

        contest.setPhaseIStartTime(contestDto.getPhaseIStartTime());
        contest.setPhaseIEndTime(contestDto.getPhaseIEndTime());
        contest.setPhaseIIStartTime(contestDto.getPhaseIIStartTime());
        contest.setPhaseIIEndTime(contestDto.getPhaseIIEndTime());

        Category category = categoryRepository.findByName(contestDto.getCategory());
        contest.setCategory(category);

        return contest;
    }

    private void validatePhaseTimings(ContestDto contestDto) {
        long phaseIDuration = getDurationInDays(contestDto.getPhaseIStartTime(), contestDto.getPhaseIEndTime());
        if (phaseIDuration < 1 || phaseIDuration > 30) {
            throw new IllegalArgumentException("Phase I duration must be between 1 day and 1 month.");
        }

        long phaseIIDuration = getDurationInHours(contestDto.getPhaseIIStartTime(), contestDto.getPhaseIIEndTime());
        if (phaseIIDuration < 1 || phaseIIDuration > 24) {
            throw new IllegalArgumentException("Phase II duration must be between 1 hour and 1 day.");
        }
    }

    private long getDurationInDays(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toDays();
    }

    private long getDurationInHours(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toHours();
    }
}