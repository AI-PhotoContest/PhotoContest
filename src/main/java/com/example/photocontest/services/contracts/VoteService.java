package com.example.photocontest.services.contracts;


import com.example.photocontest.models.Vote;
import com.example.photocontest.models.dto.VoteDto;

import java.util.Optional;

public interface VoteService {

    Vote saveVote(int photoPostId, int judgeId, VoteDto voteDto);

}
