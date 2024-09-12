package com.example.photocontest.controllers.mvc;

import com.example.photocontest.filters.ContestFilterOptions;
import com.example.photocontest.mappers.ContestMapper;
import com.example.photocontest.mappers.VoteMapper;
import com.example.photocontest.models.Contest;
import com.example.photocontest.repositories.UserRepository;
import com.example.photocontest.services.contracts.ContestService;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/contests")
public class ContestMvcController {

    private final ContestService contestService;
    private final ContestMapper contestMapper;
    private final UserService userService;
    private final PhotoPostService photoPostService;
    private final UserRepository userRepository;
    private final VoteMapper voteMapper;

    @Autowired
    public ContestMvcController(ContestService contestService,
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

    @GetMapping
    public String getAllContests(
//            @RequestParam(value = "title", required = false) String title,
//            @RequestParam(value = "category", required = false) String category,
//            @RequestParam(value = "type", required = false) String type,
//            @RequestParam(value = "phase", required = false) String phase,
            Model model) {

//        ContestFilterOptions filterOptions = new ContestFilterOptions();
//        filterOptions.setTitle(title);
//        filterOptions.setCategory(category);
//        filterOptions.setType(type);
//        filterOptions.setPhase(phase);

//        Page<Contest> contestsPage = contestService.searchContests(filterOptions, pageable);

        List<Contest> contests = contestService.getAllContests();



        model.addAttribute("contests", contests);
        model.addAttribute("active", "contests");
//        model.addAttribute("currentPage", pageable.getPageNumber());
//        model.addAttribute("totalPages", contests.getTotalPages());
//        model.addAttribute("pageSize", pageable.getPageSize());

        return "contests-page";
    }

}