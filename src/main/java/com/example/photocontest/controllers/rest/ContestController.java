package com.example.photocontest.controllers.rest;

import com.example.photocontest.exceptions.EntityNotFoundException;
import com.example.photocontest.filters.ContestFilterOptions;
import com.example.photocontest.mappers.ContestMapper;
import com.example.photocontest.models.Contest;
import com.example.photocontest.models.User;
import com.example.photocontest.models.dto.ContestDto;
import com.example.photocontest.services.contracts.ContestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/contests")
public class ContestController {

    private final ContestService contestService;
    private final ContestMapper contestMapper;

    @Autowired
    public ContestController(ContestService contestService, ContestMapper contestMapper) {
        this.contestService = contestService;
        this.contestMapper = contestMapper;
    }

    /**
     * Creates a new contest.
     *<p>
     * Example usage with Postman:
     * ```
     * POST http://localhost:8080/api/contests
     *<p>
     * Request Body:
     * {
     *   "title": "New Contest",
     *   "description": "This is a new contest",
     *   "photoUrl": "http://example.com/photo.jpg",
     *   "startDate": "2024-09-01",
     *   "endDate": "2024-09-30",
     *   "status": "OPEN",
     *   "phase": "PHASE1",
     *   "category": "Photography"
     * }
     * ```
     */
    @PostMapping
    public Contest createContest(@Valid @RequestBody ContestDto contestDto, @AuthenticationPrincipal User creator) {
        Contest contest = contestMapper.fromDto(contestDto, creator);
        return contestService.createContest(contest);
    }

    /**
     * Updates an existing contest with new details.
     * <p>
     * Example usage with Postman:
     * ```
     * PUT http://localhost:8080/api/contests/{id}
     * <p>
     * Request Body:
     * {
     *   "title": "Updated Contest Title",
     *   "description": "Updated description",
     *   "photoUrl": "http://example.com/newphoto.jpg",
     *   "startDate": "2024-10-01",
     *   "endDate": "2024-10-31",
     *   "status": "INVITATIONAL",
     *   "phase": "PHASE2",
     *   "category": "Art"
     * }
     * ```
     */
    @PutMapping("/{id}")
    public Contest updateContest(
            @PathVariable int id,
            @Valid @RequestBody ContestDto contestDto) {
        Contest existingContest = contestService.getContestById(id);

        if (existingContest != null) {
            Contest updatedContest = contestMapper.updateContestFromDto(existingContest, contestDto);
            return contestService.updateContest(updatedContest);
        } else {
            throw new EntityNotFoundException("Contest", id);
        }
    }


    /**
     * This controller handles contest-related operations such as listing and filtering contests.
     * <p>
     * Example usage with Postman:
     * <p>
     * To search for contests by title, category, type, or phase, use the following query parameters:
     * <p>
     * - `title` (optional): The title of the contest to search for (e.g., `Photo Contest`).
     * <p>
     * - `category` (optional): The category of the contest to filter by (e.g., `Photography`).
     * <p>
     * - `type` (optional): The type of contest (e.g., `Open`, `Invitational`).
     * <p>
     * - `phase` (optional): The current phase of the contest (e.g., `Phase1`, `Phase2`, `Finished`).
     * <p>
     * - `page` (optional): The page number to retrieve (e.g., `0` for the first page).
     * <p>
     * - `size` (optional): The number of contests per page (e.g., `10`).
     * <p>
     * - `sort` (optional): The sorting criteria (e.g., `title,desc` for descending order by title).
     * <p>
     * Example Postman request:
     * <p>
     * ```
     *  http://localhost:8080/contests?title=Photo Contest&category=Photography&phase=Open&page=0&size=10&sort=title,desc
     * ```
     * <p>
     * This request will search for contests with the title "Photo Contest", in the category "Photography", and in the "Open" phase,
     * returning results on the first page with 10 contests per page, sorted by title in descending order.
     * <p>
     * The filtering and sorting are handled by `JpaSpecificationExecutor`, which dynamically builds queries based on the provided parameters.
     * Each query parameter is converted into a `Specification`, and these specifications are combined to form a single query.
     * The sorting is managed through the `Sort` object passed to the `findAll` method, allowing for dynamic and flexible query execution.
     */
    @GetMapping
    public Page<Contest> getAllContests(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "phase", required = false) String phase,
            Pageable pageable) {

        ContestFilterOptions filterOptions = new ContestFilterOptions();
        filterOptions.setTitle(title);
        filterOptions.setCategory(category);
        filterOptions.setType(type);
        filterOptions.setPhase(phase);

        return contestService.searchContests(filterOptions, pageable);
    }

    @GetMapping("/{id}")
    public Contest getContestById(@PathVariable int id) {
        return contestService.getContestById(id);
    }



}
