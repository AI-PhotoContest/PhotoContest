package com.example.photocontest.helpers;

import com.example.photocontest.models.Role;
import com.example.photocontest.models.User;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.security.Principal;
@Component
public class AuthenticationHelpers {

    private static UserService userService;

    @Autowired
    public AuthenticationHelpers(UserService userService) {
        AuthenticationHelpers.userService = userService;
    }

    public static final String USER_NOT_FOUND = "User not found";


    public static boolean checkPermission(Principal principal, String requestedRole) {
        User user = userService.findUserByUsername(principal.getName());
        return checkPermission(user, requestedRole);
    }

    public static boolean checkPermission(User user, String requestedRole) {
        for (Role role : user.getRoles()) {
            if (role.getName().name().equalsIgnoreCase(requestedRole)) {
                return true;
            }
        }
        throw new SecurityException("You do not have permission to perform this operation");
    }

    public static boolean checkPermission(Principal principal , String requestedRole,String requestedRole2){
        User user = userService.findUserByUsername(principal.getName());
        return checkPermission(user,requestedRole,requestedRole2);
    }

    public static boolean checkPermission(User user , String requestedRole,String requestedRole2) {
        for (Role role : user.getRoles()) {
            if (role.getName().name().equalsIgnoreCase(requestedRole) || role.getName().name().equalsIgnoreCase(requestedRole2)) {
                return true;
            }
        }
        throw new SecurityException("You do not have permission to perform this operation");
    }

    public static boolean checkIfCurrentUserIsTheCreator(Principal principal, User user) {
        User currentUser = userService.findUserByUsername(principal.getName());
        if (currentUser.getId() == user.getId()) {
            return true;
        }
        throw new SecurityException("You do not have permission to perform this operation");
    }

    public static void checkUserExist(User user) {
        if (user == null) {
            throw new IllegalArgumentException(USER_NOT_FOUND);
        }
    }

    public static boolean checkIfUserIsVotable(Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        if (user.isVotable()) {
            return true;
        }
        throw new SecurityException("You do not have permission to perform this operation");
    }

    public static User extractUserFromProvider(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication instanceof OAuth2AuthenticationToken) {
                // OAuth2 authentication
                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                OAuth2User oauthUser = oauthToken.getPrincipal();
                String email = oauthUser.getAttribute("email"); // Replace with the actual attribute name

                return userService.findUserByEmail(email);
            } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
                // Standard login authentication
                String email = authentication.getName();

                User user = userService.findUserByUsername(email);
                return user;
            }
        }
        return null;
    }


}
