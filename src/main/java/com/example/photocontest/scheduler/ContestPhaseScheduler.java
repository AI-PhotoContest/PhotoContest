package com.example.photocontest.scheduler;

import com.example.photocontest.models.Contest;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.enums.ContestPhase;
import com.example.photocontest.services.contracts.ContestService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ContestPhaseScheduler {

    private final ContestService contestService;

    public ContestPhaseScheduler(ContestService contestService) {
        this.contestService = contestService;
    }

    @Scheduled(fixedRate = 60000) // Проверява на всеки 60 секунди
    public void checkAndAdvanceContestPhase() {
        List<Contest> contests = contestService.getAllContests();

        for (Contest contest : contests) {
            LocalDateTime now = LocalDateTime.now();

            if (contest.getPhaseIEndTime().isBefore(now) && contest.getPhase() == ContestPhase.PHASE1) {
                contest.setPhase(ContestPhase.PHASE2);
                contestService.updateContest(contest);
            }

            if (contest.getPhaseIIEndTime().isBefore(now) && contest.getPhase() == ContestPhase.PHASE2) {
                contest.setPhase(ContestPhase.FINISHED);
                List<PhotoPost> winners = contestService.findTop3ByContestId(contest.getId());
                contest.setWinner(winners.get(0));
                promoteWinners(winners);
                contestService.updateContest(contest);
            }


        }
    }

    private void promoteWinners(List<PhotoPost> winners) {
        if (winners.size() > 0) {
            // Точки за първо място
            int firstPlacePoints = 50;
            int secondPlacePoints = 35;
            int thirdPlacePoints = 20;

            // Проверка за изравнено първо място (shared 1st)
            if (winners.size() > 1 && winners.get(0).getScore() == winners.get(1).getScore()) {
                firstPlacePoints = 40;
            }

            // Добавяне на точки за първо място
            winners.get(0).getCreator().getPoints().addPoints(firstPlacePoints);

            // Проверка за двойно повече точки от второто място
            if (winners.size() > 1 && winners.get(0).getScore() >= 2 * winners.get(1).getScore()) {
                winners.get(0).getCreator().getPoints().addPoints(75);
            }

            // Добавяне на точки за второ място, ако има такова
            if (winners.size() > 1) {
                // Проверка за изравнено второ място (shared 2nd)
                if (winners.size() > 2 && winners.get(1).getScore() == winners.get(2).getScore()) {
                    secondPlacePoints = 25;
                }
                winners.get(1).getCreator().getPoints().addPoints(secondPlacePoints);
            }

            // Добавяне на точки за трето място, ако има такова
            if (winners.size() > 2) {
                // Проверка за изравнено трето място (shared 3rd)
                if (winners.size() > 3 && winners.get(2).getScore() == winners.get(3).getScore()) {
                    thirdPlacePoints = 10;
                }
                winners.get(2).getCreator().getPoints().addPoints(thirdPlacePoints);
            }
        }
    }
}