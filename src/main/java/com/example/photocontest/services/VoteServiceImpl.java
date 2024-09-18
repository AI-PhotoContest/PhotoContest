package com.example.photocontest.services;

import com.example.photocontest.mappers.VoteMapper;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import com.example.photocontest.models.Vote;
import com.example.photocontest.models.dto.VoteDto;
import com.example.photocontest.repositories.VoteRepository;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import com.example.photocontest.services.contracts.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteMapper mapper;
    private final VoteRepository voteRepository;
    private final UserService userService;
    private final PhotoPostService photoPostService;

    @Autowired
    public VoteServiceImpl(VoteMapper mapper, VoteRepository voteRepository, UserService userService, PhotoPostService photoPostService) {
        this.mapper = mapper;
        this.voteRepository = voteRepository;
        this.userService = userService;
        this.photoPostService = photoPostService;
    }

    @Override
    public Vote saveVote(int photoPostId, int judgeId, VoteDto voteDto) {
        Optional<Vote> existingVote = voteRepository.findByPhotoPostIdAndJudgeId(photoPostId, judgeId);
        User judge = userService.findUserById(judgeId);
        PhotoPost post = photoPostService.getPhotoPostById(photoPostId);
        Vote vote = mapper.toEntity(voteDto,judge,post);
        return voteRepository.save(vote);
    }

    @Override
    public Optional<Vote> findVoteById(int id) {
        return voteRepository.findById(id);
    }
}
