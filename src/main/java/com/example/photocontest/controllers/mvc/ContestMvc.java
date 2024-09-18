package com.example.photocontest.controllers.mvc;

import com.example.photocontest.exceptions.EntityNotFoundException;
import com.example.photocontest.filters.ContestFilterOptions;
import com.example.photocontest.mappers.ContestMapper;
import com.example.photocontest.mappers.PhotoPostMapper;
import com.example.photocontest.models.*;
import com.example.photocontest.models.dto.ContestDto;
import com.example.photocontest.models.dto.PhotoPostDto;
import com.example.photocontest.models.dto.VoteDto;
import com.example.photocontest.models.enums.ContestStatus;
import com.example.photocontest.repositories.CategoryRepository;
import com.example.photocontest.repositories.TagRepository;
import com.example.photocontest.services.contracts.ContestService;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import com.example.photocontest.services.contracts.VoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.photocontest.helpers.AuthenticationHelpers.checkPermission;
import static com.example.photocontest.helpers.AuthenticationHelpers.extractUserFromProvider;

@Controller
@RequestMapping("/contests")
public class ContestMvc extends BaseController {

    private final ContestService contestService;
    private final CategoryRepository categoryRepository;
    private final ContestMapper contestMapper;
    private final PhotoPostService photoPostService;
    private final TagRepository tagRepository;
    private final PhotoPostMapper mapper;
    private final PhotoPostMapper photoPostMapper;
    private final VoteService voteService;
    private final UserService userService;

    @Autowired
    public ContestMvc(ContestService contestService, CategoryRepository categoryRepository,
                      ContestMapper contestMapper, PhotoPostService photoPostService,
                      TagRepository tagRepository, PhotoPostMapper mapper, PhotoPostMapper photoPostMapper, VoteService voteService, UserService userService) {
        this.contestService = contestService;
        this.categoryRepository = categoryRepository;
        this.contestMapper = contestMapper;
        this.photoPostService = photoPostService;
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.photoPostMapper = photoPostMapper;
        this.voteService = voteService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllContests(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "phase", required = false) String phase,
            Pageable pageable,
            Model model) {

        ContestFilterOptions filterOptions = new ContestFilterOptions();
        filterOptions.setTitle(title);
        filterOptions.setCategory(category);
        filterOptions.setType(type);
        filterOptions.setPhase(phase);

        Page<Contest> contestsPage = contestService.searchContests(filterOptions, pageable);

//        List<Contest> contests = contestService.getAllContests();

        model.addAttribute("category", category);
        model.addAttribute("contests", contestsPage.getContent());
        model.addAttribute("active", "contests");
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", contestsPage.getTotalPages());
        model.addAttribute("pageSize", pageable.getPageSize());

        return "contest-pages/contests-page";
    }

    @GetMapping("/{id}")
    public String getContestById(@PathVariable int id, Model model,Authentication authentication) {
        Contest contest = contestService.getContestById(id);
        List<PhotoPost> contestPosts = photoPostService.findByContest(contest.getId());

        User loggedInUser = extractUserFromProvider(authentication);

        if (loggedInUser != null) {
            // Check if the logged-in user is a judge for this contest
            boolean isJudge = contest.getJudges().stream()
                    .anyMatch(judge -> judge.getId() == loggedInUser.getId());
            model.addAttribute("isJudge", isJudge);
        }


        model.addAttribute("posts", contestPosts);
        model.addAttribute("contest", contest);

        return "contest-pages/contest-details";
    }

    @GetMapping("/create")
    public String createContest(Authentication authentication,Model model) {
        User currentUser = extractUserFromProvider(authentication);
        checkPermission(currentUser, "ORGANIZER");
        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("contest", new ContestDto());
        model.addAttribute("statuses", ContestStatus.values());
        model.addAttribute("categories", categories);


        return "contest-pages/contest-create";
    }

    @PostMapping("/create")
    public String createContest(
            @Valid @ModelAttribute("contest") ContestDto contestDto,
            @RequestParam("photo") MultipartFile photoFile,
            Authentication authentication
    ) throws IOException {
        User currentUser = extractUserFromProvider(authentication);
        checkPermission(currentUser, "ORGANIZER");

        // Обработка на файла
        if (!photoFile.isEmpty()) {
            // Генериране на уникално име за файла
            String filename = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
            String uploadDir = "src/main/resources/static/images/contest-images/";

            Contest contest = contestMapper.fromDto(contestDto, currentUser);
            // Запазване на файла в директорията
            Files.copy(photoFile.getInputStream(), Paths.get(uploadDir + filename));

            // Запазване на URL-то за снимката в DTO-то
            contest.setPhotoUrl(filename);  // Сетва URL като String
            contestService.createContest(contest);
        }

        return "redirect:/";
    }

    @GetMapping("/rewards")
    public String getRewardsPage() {
        return "contest-pages/rewards-page";
    }


    @GetMapping("/{contestId}/add-photo-post")
    public String showPostCreatePage(@PathVariable int contestId,Model model) {
        List<Tag> tags = tagRepository.findAll();
        Contest contest = contestService.getContestById(contestId);
        PhotoPost photoPost = new PhotoPost();
        photoPost.setContest(contest);

        model.addAttribute("tags", tags);
        model.addAttribute("post", photoPost);
        return "post-pages/photo-post-create";
    }

    @PostMapping("/{contestId}/add-photo-post")
    public String createPost(@Valid @ModelAttribute("post") PhotoPostDto photoPostDto,
                             @RequestParam("photoImg") MultipartFile photoFile,
                             BindingResult result, Model model,
                             @PathVariable int contestId,
                             Authentication authentication) {
        User currentUser = extractUserFromProvider(authentication);
//        checkPermission(currentUser, "PHOTO_JUNKIE");
        if (result.hasErrors()) {
            return "post-pages/photo-post-create";
        }
        PhotoPost photoPost = mapper.toEntity(photoPostDto);
        Contest contest = contestService.getContestById(contestId);
        photoPost.setContest(contest);
        photoPostDto.setTags(convertStringToTags(photoPostDto.getTagsInput()));

        try {
            PhotoPost post = photoPostMapper.toEntity(photoPostDto);
            // Обработка на файла
            if (!photoFile.isEmpty()) {
                // Генериране на уникално име за файла
                String filename = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
                String uploadDir = "src/main/resources/static/images/photo-posts-images/";

                post.setImage(filename);
                post.setCreator(currentUser);

                // Запазване на файла в директорията
                Files.copy(photoFile.getInputStream(), Paths.get(uploadDir + filename));

                photoPostService.createPhotoPost(post);
            }
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "post-pages/photo-post-create";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<Tag> convertStringToTags(String tagsInput) {
        return Arrays.stream(tagsInput.split(","))
                .map(String::trim)
                .map(tagName -> {
                    Tag tag = new Tag();
                    tag.setName(tagName);
                    // Optionally, you can look up existing tags in the database to avoid duplicates
                    return tag;
                })
                .collect(Collectors.toSet());
    }


    @GetMapping("/{contestId}/judge-photo-post/{postId}")
    public String ratePhotoPost(
            @PathVariable int contestId,
            @PathVariable int postId,
            Model model) {

        // Fetch the contest and photo post details
        Contest contest = contestService.getContestById(contestId);
        PhotoPost post = photoPostService.getPhotoPostById(postId);

        // Ensure that the judge has not already voted on this post (optional)
        VoteDto voteDto = new VoteDto(); // Prepare an empty DTO for the form

        // Add data to the model
        model.addAttribute("contest", contest);
        model.addAttribute("post", post);
        model.addAttribute("vote", voteDto); // For the form submission

        return "contest-pages/judge-page"; // Return the page with the form for voting
    }


    @PostMapping("/{contestId}/judge-photo-post/{postId}")
    public String ratePhotoPost(
            @PathVariable int contestId,
            @PathVariable int postId,
            @Valid @ModelAttribute("vote") VoteDto voteDto,
            BindingResult bindingResult, // To handle validation errors
            Model model,
            Authentication authentication) {

        if (bindingResult.hasErrors()) {
            // Add errors to the model
            model.addAttribute("vote", voteDto);
            return "/contest-pages/judge-page"; // Reload the page with validation errors
        }

        try {
            // Get the logged-in user
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();

            // Fetch the User entity using the username (or another method)
            User judge = userService.findUserByUsername(username);

            // Save the vote
            voteService.saveVote(postId, judge.getId(), voteDto);

        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "error"; // Display an error page if something goes wrong
        }

        return "redirect:/contests/" + contestId + "/photo-posts/" + postId;
    }
}