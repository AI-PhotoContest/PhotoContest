package com.example.photocontest.controllers.mvc;

import com.example.photocontest.filters.ContestFilterOptions;
import com.example.photocontest.mappers.ContestMapper;
import com.example.photocontest.mappers.VoteMapper;
import com.example.photocontest.models.Category;
import com.example.photocontest.models.Contest;
import com.example.photocontest.models.User;
import com.example.photocontest.models.dto.ContestDto;
import com.example.photocontest.models.enums.ContestStatus;
import com.example.photocontest.repositories.CategoryRepository;
import com.example.photocontest.repositories.UserRepository;
import com.example.photocontest.services.contracts.ContestService;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static com.example.photocontest.helpers.AuthenticationHelpers.checkPermission;
import static com.example.photocontest.helpers.AuthenticationHelpers.extractUserFromProvider;

@Controller
@RequestMapping("/contests")
public class ContestMvcController extends BaseController {

    private final ContestService contestService;
    private final CategoryRepository categoryRepository;
    private final ContestMapper contestMapper;
    private final UserService userService;
    private final PhotoPostService photoPostService;
    private final UserRepository userRepository;
    private final VoteMapper voteMapper;

    @Autowired
    public ContestMvcController(ContestService contestService, CategoryRepository categoryRepository,
                                ContestMapper contestMapper,
                                UserService userService, PhotoPostService photoPostService,
                                UserRepository userRepository, VoteMapper voteMapper) {
        this.contestService = contestService;
        this.categoryRepository = categoryRepository;
        this.contestMapper = contestMapper;
        this.userService = userService;
        this.photoPostService = photoPostService;
        this.userRepository = userRepository;
        this.voteMapper = voteMapper;
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

        model.addAttribute("contests", contestsPage.getContent());
        model.addAttribute("active", "contests");
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", contestsPage.getTotalPages());
        model.addAttribute("pageSize", pageable.getPageSize());

        return "contests-page";
    }

    @GetMapping("/{id}")
    public String getContestById(@PathVariable int id, Model model) {
        Contest contest = contestService.getContestById(id);
        model.addAttribute("contest", contest);
        return "contest-details";
    }

    @GetMapping("/create")
    public String createContest(Authentication authentication,Model model) {
        User currentUser = extractUserFromProvider(authentication);
        checkPermission(currentUser, "ORGANIZER");
        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("contest", new ContestDto());
        model.addAttribute("statuses", ContestStatus.values());
        model.addAttribute("categories", categories);


        return "contest-create";
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

}