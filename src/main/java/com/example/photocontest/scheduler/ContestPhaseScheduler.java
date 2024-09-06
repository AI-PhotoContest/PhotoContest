package com.example.photocontest.scheduler;

import com.example.photocontest.models.Contest;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.enums.ContestPhase;
import com.example.photocontest.services.contracts.ContestService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ContestPhaseScheduler {
    public static final String GREEN_COLOUR = "\u001B[32m";
    public static final String CHECK_MESSAGE = GREEN_COLOUR + "Checking contests for phase advancement - ";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final ContestService contestService;

    public ContestPhaseScheduler(ContestService contestService) {
        this.contestService = contestService;
    }

    @Scheduled(fixedRate = 60000) // Проверява на всеки 60 секунди
    public void checkAndAdvanceContestPhase() {
        List<Contest> contests = contestService.getAllContests();
        LocalDateTime now = LocalDateTime.now();
        System.out.println(CHECK_MESSAGE + now.format(DATE_TIME_FORMATTER));
        for (Contest contest : contests) {

            if (contest.getPhaseIEndTime().isBefore(now) && contest.getPhase() == ContestPhase.PHASE1) {
                contest.setPhase(ContestPhase.PHASE2);
                contestService.updateContest(contest);
                System.out.println("Contest " + contest.getTitle() + " has advanced to phase 2 - " + LocalDateTime.now().format(DATE_TIME_FORMATTER));
            }

            if (contest.getPhaseIIEndTime().isBefore(now) && contest.getPhase() == ContestPhase.PHASE2) {
                contestService.setTheDefaultVotesIfNecessary(contest);
                contest.setPhase(ContestPhase.FINISHED);
                List<PhotoPost> winners = contestService.findTop3ByContestId(contest.getId());
                if (!winners.isEmpty()) {
                    contest.setWinner(winners.get(0));
                    promoteWinners(winners);
                }
                contestService.updateContest(contest);
                System.out.println("Contest " + contest.getTitle() + " has finished - " + LocalDateTime.now().format(DATE_TIME_FORMATTER));
            }


        }
    }

    private void promoteWinners(List<PhotoPost> winners) {
        if (winners.size() > 0) {
            // Points for the winners
            int firstPlacePoints = 50;
            int secondPlacePoints = 35;
            int thirdPlacePoints = 20;

            // Check for shared 1st place
            if (winners.size() > 1 && winners.get(0).getScore() == winners.get(1).getScore()) {
                firstPlacePoints = 40;
            }

            // Add points for 1st place
            winners.get(0).getCreator().getPoints().addPoints(firstPlacePoints);
            System.out.println("User " + winners.get(0).getCreator().getUsername() + " has won " + firstPlacePoints + " points");

            // Check for double points for 1st place
            if (winners.size() > 1 && winners.get(0).getScore() >= 2 * winners.get(1).getScore()) {
                winners.get(0).getCreator().getPoints().addPoints(75);
            }

            // Add points for 2nd place if there is one
            if (winners.size() > 1) {
                // Check for shared 2nd place
                if (winners.size() > 2 && winners.get(1).getScore() == winners.get(2).getScore()) {
                    secondPlacePoints = 25;
                }
                winners.get(1).getCreator().getPoints().addPoints(secondPlacePoints);
            }

            // Add points for 3rd place if there is one
            if (winners.size() > 2) {
                // Check for shared 3rd place
                if (winners.size() > 3 && winners.get(2).getScore() == winners.get(3).getScore()) {
                    thirdPlacePoints = 10;
                }
                winners.get(2).getCreator().getPoints().addPoints(thirdPlacePoints);
            }
        }
    }
}