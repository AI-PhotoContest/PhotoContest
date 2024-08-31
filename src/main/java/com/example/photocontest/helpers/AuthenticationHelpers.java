package com.example.photocontest.helpers;

import com.example.photocontest.models.Role;
import com.example.photocontest.models.User;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        for (Role role : user.getRoles()) {
            if (role.getName().name().equalsIgnoreCase(requestedRole)) {
                return true;
            }
        }
        throw new SecurityException("You do not have permission to perform this operation");
    }

    public static boolean checkPermission(Principal principal , String requestedRole,String requestedRole2) {
        User user = userService.findUserByUsername(principal.getName());
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


}
