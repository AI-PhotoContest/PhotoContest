package com.example.photocontest.mappers;

import com.example.photocontest.models.Category;
import com.example.photocontest.models.Contest;
import com.example.photocontest.models.User;
import com.example.photocontest.models.dto.ContestDto;
import com.example.photocontest.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContestMapper {

    private final CategoryRepository categoryRepository;

    @Autowired
    public ContestMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Contest fromDto(ContestDto contestDto, User user){
        Contest contest = fromDto(contestDto);
        contest.setCreator(user);
        return contest;
    }

    public Contest fromDto(ContestDto contestDto) {
        Contest contest = new Contest();
        contest.setTitle(contestDto.getTitle());
        contest.setDescription(contestDto.getDescription());
        contest.setPhotoUrl(contestDto.getPhotoUrl());
        contest.setStartDate(contestDto.getStartDate());
        contest.setEndDate(contestDto.getEndDate());
        contest.setStatus(contestDto.getStatus());
        contest.setPhase(contestDto.getPhase());

        // Fetch category from repositories
        Category category = categoryRepository.findByName(contestDto.getCategory())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category"));

        contest.setCategory(category);

        return contest;
    }

    public Contest updateContestFromDto(Contest existingContest, ContestDto contestDto) {
        existingContest.setTitle(contestDto.getTitle());
        existingContest.setDescription(contestDto.getDescription());
        existingContest.setPhotoUrl(contestDto.getPhotoUrl());
        existingContest.setStartDate(contestDto.getStartDate());
        existingContest.setEndDate(contestDto.getEndDate());
        existingContest.setStatus(contestDto.getStatus());
        existingContest.setPhase(contestDto.getPhase());

        // Fetch category from repositories if they need to be updated
        if (contestDto.getCategory() != null) {
            Category category = categoryRepository.findByName(contestDto.getCategory())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid category"));
            existingContest.setCategory(category);
        }

        return existingContest;
    }

    public ContestDto toDto(Contest contest) {
        ContestDto contestDto = new ContestDto();
        contestDto.setTitle(contest.getTitle());
        contestDto.setDescription(contest.getDescription());
        contestDto.setPhotoUrl(contest.getPhotoUrl());
        contestDto.setStartDate(contest.getStartDate());
        contestDto.setEndDate(contest.getEndDate());
        contestDto.setStatus(contest.getStatus());
        contestDto.setPhase(contest.getPhase());
        contestDto.setCategory(contest.getCategory().getName());
        return contestDto;
    }
}