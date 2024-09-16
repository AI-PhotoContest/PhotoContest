package com.example.photocontest.controllers.mvc;

import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

import static com.example.photocontest.helpers.AuthenticationHelpers.extractUserFromProvider;

@Controller
public class HomeController extends BaseController {


    private final PhotoPostService postService;
    private final UserService userService;

    @Autowired
    public HomeController(PhotoPostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }


    @GetMapping
    public String landing() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model, Authentication authentication, PhotoPost photoPost, HttpServletRequest request) {
        List<PhotoPost> posts = postService.getAllPhotoPosts();
        if (authentication != null) {
            User user = extractUserFromProvider(authentication);
            model.addAttribute("user", user);
        }
        Collections.shuffle(posts);

        // Избиране на първите 4 поста, ако списъкът е по-голям от 4
        List<PhotoPost> randomPhotoPosts;
        if (posts.size() > 4) {
            randomPhotoPosts = posts.subList(0, 4);
        } else {
            randomPhotoPosts = posts;  // Ако има по-малко от 4 поста, вземи всички
        }
        model.addAttribute("randomPhotoPosts", randomPhotoPosts);
        model.addAttribute("active", "home");
        model.addAttribute("posts", posts);
        model.addAttribute("post", photoPost);
        model.addAttribute("httpServletRequest", request);
        return "home";
    }



}
