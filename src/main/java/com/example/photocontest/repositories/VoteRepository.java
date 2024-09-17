package com.example.photocontest.repositories;

import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import com.example.photocontest.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VoteRepository extends JpaRepository<Vote, Integer>, JpaSpecificationExecutor<Vote>{

    Vote findByPhotoPostAndJudge(PhotoPost post, User judge);
}
