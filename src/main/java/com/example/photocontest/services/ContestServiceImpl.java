package com.example.photocontest.services;

import com.example.photocontest.exceptions.EntityNotFoundException;
import com.example.photocontest.filters.ContestFilterOptions;
import com.example.photocontest.filters.specifications.ContestSpecifications;
import com.example.photocontest.models.Contest;
import com.example.photocontest.models.User;
import com.example.photocontest.repositories.ContestRepository;
import com.example.photocontest.services.contracts.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.example.photocontest.helpers.ContestHelpers.checkIfContestExists;

@Service
public class ContestServiceImpl implements ContestService {

    private final ContestRepository contestRepository;

    @Autowired
    public ContestServiceImpl(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    @Override
    public Page<Contest> searchContests(ContestFilterOptions filterOptions, Pageable pageable) {
        Specification<Contest> spec = Specification.where(ContestSpecifications.hasTitle(filterOptions.getTitle()))
                .and(ContestSpecifications.hasCategory(filterOptions.getCategory()))
                .and(ContestSpecifications.hasType(filterOptions.getType()))
                .and(ContestSpecifications.hasPhase(filterOptions.getPhase()));

        return contestRepository.findAll(spec, pageable);
    }

    @Override
    public Contest createContest(Contest contest) {
        return null;
    }

    @Override
    public Contest getContestById(int id) {
        Contest contest = contestRepository.findById(id);
        checkIfContestExists(contest, id);
        return contest;
    }

    @Override
    public Contest updateContest(Contest contest) {
        return null;
    }

    @Override
    public void deleteContest(int id) {

    }

    @Override
    public void closeContest(int id) {

    }

    @Override
    public Contest addPhotoPostToContest(Contest contest, int photoPostId) {
        return null;
    }

    @Override
    public Contest removePhotoPostFromContest(Contest contest, int photoPostId) {
        return null;
    }

    @Override
    public Contest addJudgeToContest(Contest contest, User user) {
        return null;
    }

    @Override
    public Contest removeJudgeFromContest(Contest contest, User user) {
        return null;
    }

    @Override
    public Contest ratePhotoPost(Contest contest, int photoPostId, int rating) {
        return null;
    }

    @Override
    public Contest switchContestPhase(Contest contest) {
        return null;
    }

    @Override
    public Contest switchContestStatus(Contest contest) {
        return null;
    }
}
