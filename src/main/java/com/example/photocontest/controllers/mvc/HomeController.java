package com.example.photocontest.controllers.mvc;

import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

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
    public String home(Model model, Principal principal, PhotoPost photoPost) {
        List<PhotoPost> posts = postService.getAllPhotoPosts();
        if (principal != null) {
            User user = userService.findUserByUsername(principal.getName());
        }
        model.addAttribute("active", "home");
        model.addAttribute("posts", posts);
        model.addAttribute("post", photoPost);
        return "home";
    }

}
