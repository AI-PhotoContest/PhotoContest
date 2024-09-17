package com.example.photocontest.services.contracts;

import com.example.photocontest.filters.ContestFilterOptions;
import com.example.photocontest.models.Contest;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContestService {

    Page<Contest> searchContests(ContestFilterOptions filterOptions, Pageable pageable);
    Contest createContest(Contest contest);
    Contest getContestById(int id);

    List<Contest> getAllContests();

    Contest updateContest(Contest contest);
    void deleteContest(int id);
    void closeContest(int id);
    Contest addPhotoPostToContest(Contest contest, int photoPostId);
    Contest removePhotoPostFromContest(Contest contest, int photoPostId);
    void addJudgeToContest(Contest contest, User user);
    void removeJudgeFromContest(Contest contest, User user);
    Contest ratePhotoPost(Contest contest, int photoPostId, int rating);
    Contest switchContestPhase(Contest contest);
    Contest switchContestStatus(Contest contest);


    List<PhotoPost> findTop3ByContestId(int contestId);

    void setTheDefaultVotesIfNecessary(Contest contest);
    List<Contest> getRecentContests();
}
