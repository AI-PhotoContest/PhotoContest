package com.example.photocontest.services;

import com.example.photocontest.filters.ContestFilterOptions;
import com.example.photocontest.filters.specifications.ContestSpecifications;
import com.example.photocontest.models.Contest;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import com.example.photocontest.repositories.ContestRepository;
import com.example.photocontest.repositories.PhotoPostRepository;
import com.example.photocontest.services.contracts.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.photocontest.helpers.ContestHelpers.checkIfContestExists;

@Service
public class ContestServiceImpl implements ContestService {

    private final ContestRepository contestRepository;
    private final PhotoPostRepository photoPostRepository;

    @Autowired
    public ContestServiceImpl(ContestRepository contestRepository, PhotoPostRepository photoPostRepository) {
        this.contestRepository = contestRepository;
        this.photoPostRepository = photoPostRepository;
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
        return contestRepository.save(contest);
    }

    @Override
    public Contest getContestById(int id) {
        Contest contest = contestRepository.findById(id);
        checkIfContestExists(contest, id);
        return contest;
    }

    @Override
    public List<Contest> getAllContests() {
        return contestRepository.findAll();
    }

    @Override
    public Contest updateContest(Contest contest) {
        return contestRepository.save(contest);
    }

    @Override
    public void deleteContest(int id) {

    }

    @Override
    public void closeContest(int id) {

    }

    @Override
    public Contest addPhotoPostToContest(Contest contest, int photoPostId) {
        PhotoPost photoPost = photoPostRepository.getPhotoPostById(photoPostId);
        if (photoPost == null) {
            throw new IllegalArgumentException("Photo post with id " + photoPostId + " does not exist!");
        }
        contest.getPhotoPosts().add(photoPost);
        return contestRepository.save(contest);
    }

    @Override
    public Contest removePhotoPostFromContest(Contest contest, int photoPostId) {
        return null;
    }

    @Override
    public void addJudgeToContest(Contest contest, User user) {
        if (contest.getJudges() == null) {
            List<User> judges = List.of(user);
            contest.setJudges(judges);
        }
        contest.getJudges().add(user);
    }

    @Override
    public void removeJudgeFromContest(Contest contest, User user) {
        if (contest.getJudges() != null) {
            contest.getJudges().remove(user);
        }
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

    @Override
    public List<PhotoPost> findTop3ByContestId(int contestId) {
        List<PhotoPost> allPosts = contestRepository.findAllByContestId(contestId);
        return allPosts.stream()
                .sorted(Comparator.comparing(PhotoPost::getScore).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }
}
