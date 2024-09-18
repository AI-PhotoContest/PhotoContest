package com.example.photocontest.controllers.mvc;

import com.example.photocontest.models.User;
import com.example.photocontest.models.enums.RoleType;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.photocontest.helpers.AuthenticationHelpers.checkPermission;
import static com.example.photocontest.helpers.AuthenticationHelpers.extractUserFromProvider;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController extends BaseController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(@RequestParam(value = "search", required = false) String searchQuery,
                            Model model, Authentication authentication) {
        User loggedUser = extractUserFromProvider(authentication);
        checkPermission(loggedUser, "ADMIN");

        List<User> users;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            users = userService.searchUsersByUsername(searchQuery);
        } else {
            users = userService.findAllUsers();
        }
        model.addAttribute("users", users);
        model.addAttribute("searchQuery", searchQuery);
        return "user-pages/admin-page";
    }

    @PostMapping("/promote")
    public String promoteToOrganizer(@RequestParam("userId") int userId) {
        userService.promoteUserToOrganizer(userId);
        return "redirect:/admin/users";
    }

    @PostMapping("/ban")
    public String banUser(@RequestParam("userId") int userId) {
        userService.banUser(userId);
        return "redirect:/admin/users";
    }

    @PostMapping("/unban")
    public String unbanUser(@RequestParam("userId") int userId) {
        userService.unbanUser(userId);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("userId") int userId) {
        userService.deleteUser(userId);
        return "redirect:/admin/users";
    }

}