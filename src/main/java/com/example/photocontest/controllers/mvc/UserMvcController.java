package com.example.photocontest.controllers.mvc;

import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import com.example.photocontest.models.dto.UserDto;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController extends BaseController{

    private final UserService userService;
    private final PhotoPostService photoPostService;

    @Autowired
    public UserMvcController(UserService userService, PhotoPostService photoPostService) {
        this.userService = userService;
        this.photoPostService = photoPostService;
    }

    public String getUsers(Model model, UserDto userDto) {
        List<User> users = userService.getAll();
        model.addAttribute("active", "users");
        model.addAttribute("users", users);
        model.addAttribute("user", userDto);
        return "user-pages/user-page";
    }

    @GetMapping("/{id}")
    public String getUser(Model model, @ModelAttribute("id") int id) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        List<PhotoPost> userPosts = photoPostService.findByCreatedBy(user);
        model.addAttribute("userPostsSize", userPosts.size());
        return "user-pages/user-page";
    }

    @GetMapping("/user/posts")
    public String getUserPosts(Model model, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        List<PhotoPost> userPosts = photoPostService.findByCreatedBy(user);
        model.addAttribute("user", user);
        model.addAttribute("photoPosts", userPosts);
        return "post-pages/photo-posts";
    }

    @GetMapping("/stats")
    public String getUserStats() {
        return "user-pages/user-stats";
    }

}
