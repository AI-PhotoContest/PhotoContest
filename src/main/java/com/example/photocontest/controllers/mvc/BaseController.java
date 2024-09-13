package com.example.photocontest.controllers.mvc;

import com.example.photocontest.models.User;
import com.example.photocontest.security.CustomOAuth2User;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class BaseController {

    @Autowired
    private UserService userService;

    @ModelAttribute("loggedInUser")
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Проверка дали authentication е null, не е анонимен и е аутентициран
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                return convertToUser((UserDetails) authentication.getPrincipal());
            } else if (authentication.getPrincipal() instanceof OAuth2User) {
                return convertToUser(((OAuth2User) authentication.getPrincipal()).getAttribute("email").toString());
            }
        }
        return null; // Връщаме null, ако не е логнат
    }

    private User convertToUser(UserDetails userDetails) {
        // Създайте нов User и задайте необходимите полета на базата на UserDetails
        User user = new User();
        user = userService.findUserByUsername(userDetails.getUsername());

        // Добавете другите полета, които са необходими
        return user;
    }

    private User convertToUser(String email) {
        // Създайте нов User и задайте необходимите полета на базата на UserDetails
        User user = new User();
        user = userService.findUserByEmail(email);

        // Добавете другите полета, които са необходими
        return user;
    }

//    @ModelAttribute("categories")


}