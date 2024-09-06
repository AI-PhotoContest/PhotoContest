package com.example.photocontest.controllers.rest;

import com.example.photocontest.exceptions.AuthorizationException;
import com.example.photocontest.exceptions.EntityNotFoundException;
import com.example.photocontest.exceptions.InvalidContestPhaseException;
import com.example.photocontest.filters.ContestFilterOptions;
import com.example.photocontest.filters.PhotoPostFilterOptions;
import com.example.photocontest.mappers.ContestMapper;
import com.example.photocontest.mappers.VoteMapper;
import com.example.photocontest.models.Contest;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import com.example.photocontest.models.Vote;
import com.example.photocontest.models.dto.ContestDto;
import com.example.photocontest.models.dto.VoteDto;
import com.example.photocontest.models.enums.ContestPhase;
import com.example.photocontest.repositories.UserRepository;
import com.example.photocontest.services.contracts.ContestService;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.photocontest.helpers.AuthenticationHelpers.*;

@RestController
@RequestMapping("/api/contests")
public class ContestController {

    private final ContestService contestService;
    private final ContestMapper contestMapper;
    private final UserService userService;
    private final PhotoPostService photoPostService;
    private final UserRepository userRepository;
    private final VoteMapper voteMapper;

    @Autowired
    public ContestController(ContestService contestService,
                             ContestMapper contestMapper,
                             UserService userService, PhotoPostService photoPostService,
                             UserRepository userRepository, VoteMapper voteMapper) {
        this.contestService = contestService;
        this.contestMapper = contestMapper;
        this.userService = userService;
        this.photoPostService = photoPostService;
        this.userRepository = userRepository;
        this.voteMapper = voteMapper;
    }


    /**
     * Creates a new contest with specific phase time limits.
     * <p>
     * Example usage with Postman:
     * <p>
     * To create a new contest, you need to send a `POST` request with the contest details in the request body as JSON.
     * <p>
     * Example Postman request:
     * <p>
     * ```
     * POST http://localhost:8080/api/contests
     * <p>
     * Request Body:
     * {
     *   "title": "New Contest",
     *   "description": "This is a new contest",
     *   "photoUrl": "http://example.com/photo.jpg",
     *   "startDate": "2024-09-01T00:00:00",
     *   "endDate": "2024-09-30T00:00:00",
     *   "status": "OPEN",
     *   "category": "Photography",
     *   "phaseIStartTime": "2024-09-01T00:00:00",
     *   "phaseIEndTime": "2024-09-15T00:00:00",
     *   "phaseIIStartTime": "2024-09-16T00:00:00",
     *   "phaseIIEndTime": "2024-09-16T12:00:00"
     * }
     * ```
     * <p>
     * The phase time limits must adhere to the following rules:
     * <ul>
     *   <li>Phase I duration must be between 1 day and 1 month.</li>
     *   <li>Phase II duration must be between 1 hour and 1 day.</li>
     * </ul>
     * <p>
     * If these constraints are violated, the request will be rejected with an appropriate error message.
     * This ensures that the contest adheres to the required timing constraints for each phase.
     */
    @PostMapping
    public Contest createContest(@Valid @RequestBody ContestDto contestDto,
                                 Principal principal) {
        checkPermission(principal, "ORGANIZER");

        User loggedUser = userService.findUserByUsername(principal.getName());
        Contest contest = contestMapper.fromDto(contestDto, loggedUser);

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
     * "title": "Updated Contest Title",
     * "description": "Updated description",
     * "photoUrl": "http://example.com/newphoto.jpg",
     * "startDate": "2024-10-01T00:00:00",
     * "endDate": "2024-10-31T00:00:00",
     * "status": "INVITATIONAL",
     * "category": "Art"
     * }
     * ```
     */
    @PutMapping("/{id}")
    public Contest updateContest(
            @PathVariable int id,
            @Valid @RequestBody ContestDto contestDto, Principal principal) {
        checkPermission(principal, "ORGANIZER");

        Contest existingContest = contestService.getContestById(id);
        User loggedUser = userService.findUserByUsername(principal.getName());

        if (existingContest.getCreator() != loggedUser) {
            throw new IllegalArgumentException("You are not the creator of this contest");
        }

        Contest updatedContest = contestMapper.updateContestFromDto(existingContest, contestDto);
        return contestService.updateContest(updatedContest);
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
     * http://localhost:8080/contests?title=Photo Contest&category=Photography&phase=Open&page=0&size=10&sort=title,desc
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


    /**
     * Adds one or more judges to an existing contest.
     * <p>
     * Example usage with Postman:
     * <p>
     * To add judges to a contest, you need to send a `POST` request with the contest's ID in the URL path
     * and a list of judge usernames in the request body as JSON.
     * <p>
     * Example Postman request:
     * <p>
     * ```
     * POST http://localhost:8080/api/contests/{id}/judges
     * ```
     * <p>
     * Request Body:
     * ```
     * [
     * "judge1",
     * "judge2",
     * "judge3"
     * ]
     * ```
     * <p>
     * This request will add the users with the usernames "judge1", "judge2", and "judge3" as judges to the contest with the specified ID.
     * <p>
     * The method retrieves the `User` objects corresponding to the provided usernames, adds them as judges to the contest,
     * and then updates the contest to reflect these changes.
     * <p>
     *
     * @param id             the ID of the contest to which judges should be added
     * @param judgeUsernames a list of usernames of the judges to be added
     * @return the updated contest details with the new judges added
     */
    @PostMapping("/{id}/judges")
    public Contest addJudgesToContest(@PathVariable int id,
                                      @RequestBody List<String> judgeUsernames,
                                      Principal principal) {

        checkPermission(principal, "ORGANIZER");
        Contest contest = contestService.getContestById(id);

        // Fetch the User objects based on the provided usernames
        List<User> judges = judgeUsernames.stream()
                .map(userRepository::findVotableUserByUsername)
                .toList();

        // Add each judge to the contest
        for (User judge : judges) {
            contestService.addJudgeToContest(contest, judge);
        }

        return contestService.updateContest(contest);
    }

    @DeleteMapping("/{id}/judges")
    public Contest removeJudgesFromContest(@PathVariable int id,
                                           @RequestBody List<String> judgeUsernames,
                                           Principal principal) {

        checkPermission(principal, "ORGANIZER");
        Contest contest = contestService.getContestById(id);

        // Fetch the User objects based on the provided usernames
        List<User> judges = judgeUsernames.stream()
                .map(userService::findUserByUsername)
                .toList();

        // Remove each judge from the contest
        for (User judge : judges) {
            contestService.removeJudgeFromContest(contest, judge);
        }

        return contestService.updateContest(contest);
    }


    /**
     * This method retrieves a paginated list of photo posts for a specific contest.
     * <p>
     * The method allows clients to filter the photo posts by title and supports pagination and sorting.
     * <p>
     * Example usage with Postman:
     * <p>
     * To get photo posts for a specific contest with optional title filtering:
     * <p>
     * - `title` (optional): The title to filter the photo posts (e.g., `Sunset`).
     * - `page` (optional): The page number to retrieve (e.g., `0` for the first page).
     * - `size` (optional): The number of photo posts per page (e.g., `10`).
     * - `sort` (optional): The sorting criteria (e.g., `title,asc` for ascending order by title).
     * <p>
     * Example Postman request:
     * <p>
     * ```
     * GET http://localhost:8080/api/contests/{id}/photo-posts?title=Sunset&page=0&size=10&sort=title,asc
     * ```
     * <p>
     * This request will return a paginated list of photo posts for the contest with the specified ID,
     * filtered by the title "Sunset" if provided, and sorted by title in ascending order.
     *
     * @param id       the ID of the contest to retrieve photo posts for
     * @param title    the title to filter the photo posts by (optional)
     * @param pageable the pagination and sorting information
     * @return a page of photo posts matching the criteria
     */
    @GetMapping("/{id}/photo-posts")
    public Page<PhotoPost> getPhotoPostsForContest(
            @PathVariable int id,
            @RequestParam(value = "title", required = false) String title,
            Pageable pageable) {

        Contest contest = contestService.getContestById(id);

        PhotoPostFilterOptions filterOptions = new PhotoPostFilterOptions();
        filterOptions.setTitle(title);
        filterOptions.setContest(contest);

        return photoPostService.searchPhotoPosts(filterOptions, pageable);
    }


    @PostMapping("/{contestId}/photo-posts/{photoPostId}")
    public Contest addPhotoPostToContest(@PathVariable int contestId,
                                         @PathVariable int photoPostId,
                                         Principal principal) {
        checkPermission(principal, "PHOTO_JUNKIE");
        Contest contest = contestService.getContestById(contestId);
        return contestService.addPhotoPostToContest(contest, photoPostId);
    }

    @DeleteMapping("/{id}/photo-posts")
    public Contest removePhotoPostFromContest(@PathVariable int id,
                                              @RequestBody int photoPostId,
                                              Principal principal) {
        PhotoPost photoPost = photoPostService.getPhotoPostById(photoPostId);
        if (checkIfCurrentUserIsTheCreator(principal, photoPost.getCreator()) || checkPermission(principal, "ADMIN")) {
            Contest contest = contestService.getContestById(id);
            return contestService.removePhotoPostFromContest(contest, photoPostId);
        }
        throw new AuthorizationException("You are not authorized to remove this photo post from the contest");
    }



    /**
     * This method allows a user to cast a vote for a specific photo post within a given contest.
     * <p>
     * The method performs several checks and operations to ensure that the vote is valid and correctly associated
     * with the appropriate contest and photo post. It also adds the vote to the corresponding photo post and updates
     * the photo post in the database.
     * <p>
     * Example usage with Postman:
     * <p>
     * To vote for a specific photo post within a contest:
     * <p>
     * - `contestId` (required): The ID of the contest to which the photo post belongs.
     * - `photoPostId` (required): The ID of the photo post for which the vote is being cast.
     * - `voteDto` (required): The vote data that includes the score, comment, and category mismatch information.
     * <p>
     * Example Postman request:
     * <p>
     * ```
     * PUT http://localhost:8080/api/contests/{contestId}/photo-posts/{photoPostId}/vote
     * Content-Type: application/json
     * <p>
     * Body: {
     *   "score": 8,
     *   "comment": "Great composition and lighting!",
     *   "categoryMismatch": false
     * }
     * ```
     * <p>
     * This request will cast a vote for the specified photo post within the given contest.
     * The `score` must be between 1 and 10, unless `categoryMismatch` is true, in which case the `score` is automatically set to 0.
     * The `comment` field is mandatory and should provide feedback or justification for the score given.
     * <p>
     * Important Notes:
     * - The user casting the vote must be authorized and have the `votable` status.
     * - The photo post must be part of the specified contest.
     * - The method checks whether the contest and photo post exist and are valid.
     *
     * @param contestId    the ID of the contest to which the photo post belongs
     * @param photoPostId  the ID of the photo post being voted on
     * @param voteDto      the data transfer object containing the vote details (score, comment, category mismatch)
     * @param principal    the security principal representing the current authenticated user
     * @return the updated PhotoPost object after the vote has been cast
     * @throws EntityNotFoundException if the contest or photo post is not found
     * @throws SecurityException if the user is not authorized to vote
     */
    @PutMapping("/{contestId}/photo-posts/{photoPostId}/vote")
    public PhotoPost vote(@PathVariable int contestId,
                                      @PathVariable int photoPostId,
                                      @RequestBody VoteDto voteDto,
                                      Principal principal) {
        checkIfUserIsVotable(principal);

        // Check if the contest and photo post exist
        Contest contest = contestService.getContestById(contestId);
        //check if the contest is in the voting phase
        if (contest.getPhase() != ContestPhase.PHASE2) {
            throw new InvalidContestPhaseException("Contest is not in the voting phase");
        }
        PhotoPost photoPost = photoPostService.getPhotoPostById(photoPostId);
        //check if that photoPost is part of the contest
        if (!contest.getPhotoPosts().contains(photoPost)) {
            throw new EntityNotFoundException("PhotoPost", photoPostId);
        }

        User judgeUser = userService.findUserByUsername(principal.getName());
        // Create a new Vote entity from the VoteDto
        Vote vote = voteMapper.toEntity(voteDto, judgeUser , photoPost);
        photoPost.addVote(vote);
        // Update the photo post with the new vote
        return photoPostService.updatePhotoPost(photoPost);
    }


}
