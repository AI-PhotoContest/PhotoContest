package com.example.photocontest.mappers;

import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import com.example.photocontest.models.Vote;
import com.example.photocontest.models.dto.VoteDto;
import com.example.photocontest.repositories.PhotoPostRepository;
import com.example.photocontest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoteMapper {

    private final PhotoPostRepository photoPostRepository;

    @Autowired
    public VoteMapper(PhotoPostRepository photoPostRepository) {
        this.photoPostRepository = photoPostRepository;
    }

    public Vote toEntity(VoteDto voteDto, User user, int photoPostId) {
        Vote vote = new Vote();

        PhotoPost photoPost = photoPostRepository.findById(voteDto.getPhotoPostId())
                .orElseThrow(() -> new IllegalArgumentException("PhotoPost not found"));


        vote.setPhotoPost(photoPost);
        vote.setUser(user);
        vote.setScore(voteDto.getScore());
        vote.setComment(voteDto.getComment());
        vote.setCategoryMismatch(voteDto.isCategoryMismatch());

        return vote;
    }

    public VoteDto toDto(Vote vote) {
        VoteDto voteDto = new VoteDto();

        voteDto.setPhotoPostId(vote.getPhotoPost().getId());
        voteDto.setScore(vote.getScore());
        voteDto.setComment(vote.getComment());
        voteDto.setCategoryMismatch(vote.isCategoryMismatch());

        return voteDto;
    }
}