package com.example.photocontest.controllers.mvc;

import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

import static com.example.photocontest.helpers.AuthenticationHelpers.extractUserFromProvider;

@Controller
public class HomeController {


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
    public String home(Model model, Authentication authentication, PhotoPost photoPost) {
        List<PhotoPost> posts = postService.getAllPhotoPosts();
        if (authentication != null) {
            User user = extractUserFromProvider(authentication);
            model.addAttribute("user", user);
        }
        model.addAttribute("active", "home");
        model.addAttribute("posts", posts);
        model.addAttribute("post", photoPost);
        return "home";
    }

}
