package com.example.photocontest.controllers.mvc;

import com.example.photocontest.models.Category;
import com.example.photocontest.models.User;
import com.example.photocontest.models.enums.RoleType;
import com.example.photocontest.services.contracts.CategoryService;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;


    @ModelAttribute("loggedInUser")
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                return convertToUser((UserDetails) authentication.getPrincipal());
            } else if (authentication.getPrincipal() instanceof OAuth2User) {
                return convertToUser(((OAuth2User) authentication.getPrincipal()).getAttribute("email").toString());
            }
        }
        return null;
    }

    @ModelAttribute("isOrganizer")
    public boolean isOrganizer() {
        User loggedInUser = getLoggedInUser();
        if (loggedInUser != null) {
            return loggedInUser.getRoles().stream()
                    .anyMatch(role -> role.getName() == RoleType.ORGANIZER);
        }
        return false;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        User loggedInUser = getLoggedInUser();
        if (loggedInUser != null) {
            return loggedInUser.getRoles().stream()
                    .anyMatch(role -> role.getName() == RoleType.ADMIN);
        }
        return false;
    }

    @ModelAttribute("categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }


    private User convertToUser(UserDetails userDetails) {

        User user = new User();
        user = userService.findUserByUsername(userDetails.getUsername());

        return user;
    }

    private User convertToUser(String email) {

        User user = new User();
        user = userService.findUserByEmail(email);

        return user;
    }


}