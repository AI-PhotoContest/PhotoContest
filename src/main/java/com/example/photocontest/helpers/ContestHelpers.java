package com.example.photocontest.helpers;

import com.example.photocontest.exceptions.EntityNotFoundException;
import com.example.photocontest.models.Contest;

public class ContestHelpers {

    public static void checkIfContestExists(Contest contest, int id) {
        if (contest == null) {
            throw new EntityNotFoundException("Contest",id);
        }
    }

}
