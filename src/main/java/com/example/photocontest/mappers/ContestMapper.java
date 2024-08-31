package com.example.photocontest.mappers;

import com.example.photocontest.models.Category;
import com.example.photocontest.models.Contest;
import com.example.photocontest.models.User;
import com.example.photocontest.models.dto.ContestDto;
import com.example.photocontest.models.enums.ContestPhase;
import com.example.photocontest.repositories.CategoryRepository;
import com.example.photocontest.services.CategoryServiceImpl;
import com.example.photocontest.services.contracts.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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

        contest.setStartDate(convertToLocalDate(contestDto.getStartDate()));
        contest.setEndDate(convertToLocalDate(contestDto.getEndDate()));
        contest.setStatus(contestDto.getStatus());
        contest.setPhase(ContestPhase.PHASE1);

        contest.setPhaseIStartTime(convertToLocalDate(contestDto.getPhaseIStartTime()));
        contest.setPhaseIEndTime(convertToLocalDate(contestDto.getPhaseIEndTime()));
        contest.setPhaseIIStartTime(convertToLocalDate(contestDto.getPhaseIIStartTime()));
        contest.setPhaseIIEndTime(convertToLocalDate(contestDto.getPhaseIIEndTime()));

        Category category = categoryRepository.findByName(contestDto.getCategory());
        if (category == null) {
            categoryService.createCategory(contestDto.getCategory());
        }

        contest.setCategory(category);

        return contest;
    }

    public ContestDto toDto(Contest contest) {
        ContestDto contestDto = new ContestDto();
        contestDto.setTitle(contest.getTitle());
        contestDto.setDescription(contest.getDescription());
        contestDto.setPhotoUrl(contest.getPhotoUrl());

        contestDto.setStartDate(convertToDate(contest.getStartDate()));
        contestDto.setEndDate(convertToDate(contest.getEndDate()));
        contestDto.setStatus(contest.getStatus());

        contestDto.setPhaseIStartTime(convertToDate(contest.getPhaseIStartTime()));
        contestDto.setPhaseIEndTime(convertToDate(contest.getPhaseIEndTime()));
        contestDto.setPhaseIIStartTime(convertToDate(contest.getPhaseIIStartTime()));
        contestDto.setPhaseIIEndTime(convertToDate(contest.getPhaseIIEndTime()));

        contestDto.setCategory(contest.getCategory().getName());

        return contestDto;
    }

    public Contest updateContestFromDto(Contest contest, ContestDto contestDto) {
        validatePhaseTimings(contestDto);

        contest.setTitle(contestDto.getTitle());
        contest.setDescription(contestDto.getDescription());
        contest.setPhotoUrl(contestDto.getPhotoUrl());

        contest.setStartDate(convertToLocalDate(contestDto.getStartDate()));
        contest.setEndDate(convertToLocalDate(contestDto.getEndDate()));
        contest.setStatus(contestDto.getStatus());

        contest.setPhaseIStartTime(convertToLocalDate(contestDto.getPhaseIStartTime()));
        contest.setPhaseIEndTime(convertToLocalDate(contestDto.getPhaseIEndTime()));
        contest.setPhaseIIStartTime(convertToLocalDate(contestDto.getPhaseIIStartTime()));
        contest.setPhaseIIEndTime(convertToLocalDate(contestDto.getPhaseIIEndTime()));

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

    private long getDurationInDays(Date start, Date end) {
        LocalDate startDate = convertToLocalDate(start);
        LocalDate endDate = convertToLocalDate(end);
        return Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
    }

    private long getDurationInHours(Date start, Date end) {
        return Duration.between(start.toInstant(), end.toInstant()).toHours();
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}