package com.example.photocontest.services;

import com.example.photocontest.exceptions.InvalidContestPhaseException;
import com.example.photocontest.filters.ContestFilterOptions;
import com.example.photocontest.filters.specifications.ContestSpecifications;
import com.example.photocontest.models.Contest;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import com.example.photocontest.models.Vote;
import com.example.photocontest.models.enums.ContestPhase;
import com.example.photocontest.repositories.ContestRepository;
import com.example.photocontest.repositories.PhotoPostRepository;
import com.example.photocontest.repositories.UserRepository;
import com.example.photocontest.services.contracts.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.photocontest.helpers.ContestHelpers.checkIfContestExists;

@Service
public class ContestServiceImpl implements ContestService {

    public static final String DEFAULT_VOTE_COMMENT = "Photo is not reviewed, default score of 3 is given.";
    public static final String CANNOT_ADD_PHOTO_TO_PHASE2_ERROR_MESSAGE = "Cannot add photo post to contest. Contest is not in PHASE1.";
    private final ContestRepository contestRepository;
    private final PhotoPostRepository photoPostRepository;
    private final UserRepository userRepository;

    @Autowired
    public ContestServiceImpl(ContestRepository contestRepository, PhotoPostRepository photoPostRepository, UserRepository userRepository) {
        this.contestRepository = contestRepository;
        this.photoPostRepository = photoPostRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<Contest> searchContests(ContestFilterOptions filterOptions, Pageable pageable) {
        Specification<Contest> spec = Specification.where(ContestSpecifications.hasTitle(filterOptions.getTitle()))
                .and(ContestSpecifications.hasCategory(filterOptions.getCategory()))
                .and(ContestSpecifications.hasType(filterOptions.getType()))
                .and(ContestSpecifications.hasPhase(filterOptions.getPhase()));
            Page<Contest> contests = contestRepository.findAll(spec, pageable);
        return contests;
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
        checkStage(contest);
        PhotoPost photoPost = photoPostRepository.getPhotoPostById(photoPostId);
        if (photoPost == null) {
            throw new IllegalArgumentException("Photo post with id " + photoPostId + " does not exist!");
        }
        if (contest.getPhotoPosts().contains(photoPost)) {
            throw new IllegalArgumentException("Photo post with id " + photoPostId + " is already in the contest!");
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

    private void checkStage(Contest contest){
        if(contest.getPhase().equals(ContestPhase.PHASE2)){
            throw new InvalidContestPhaseException(CANNOT_ADD_PHOTO_TO_PHASE2_ERROR_MESSAGE);
        }
    }

   public void setTheDefaultVotesIfNecessary(Contest contest) {
       for (PhotoPost photoPost : contest.getPhotoPosts()) {
           Vote defaultVote = new Vote();
           defaultVote.setComment(DEFAULT_VOTE_COMMENT);
           if (photoPost.getVotes() == null) {
               photoPost.addVote(defaultVote);
           }

       }
   }

    @Override
    public List<Contest> getRecentContests() {
        return contestRepository.findRecentContests(PageRequest.of(0, 10)); // Fetch top 10 recent contests
    }

    // Get a random contest from recent ones
    public Optional<Contest> getRandomRecentContest() {
        List<Contest> recentContests = getRecentContests();
        if (!recentContests.isEmpty()) {
            Collections.shuffle(recentContests);
            return Optional.of(recentContests.get(0)); // Return a random contest
        }
        return Optional.empty(); // Return empty if no contests are available
    }


    @Override
    public void addJudgeToContest(int contestId, int userId) {
        Contest contest = contestRepository.findById(contestId);
        User user = userRepository.findById(userId);
        contest.getJudges().add(user);
        contestRepository.save(contest);
    }

}
