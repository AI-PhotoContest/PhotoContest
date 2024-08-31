package com.example.photocontest.helpers;

public class VoteHelpers {

    public static int calculateVote(int vote, int maxVote) {
        if (vote < 0) {
            return 0;
        } else if (vote > maxVote) {
            return maxVote;
        } else {
            return vote;
        }
    }


}
