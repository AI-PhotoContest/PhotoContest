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

    public static void checkPermission(Principal principal, String requestedRole) {
        User user = userService.findUserByUsername(principal.getName());
        for (Role role : user.getRoles()) {
            if (role.getName().name().equalsIgnoreCase(requestedRole)) {
                return;
            }
        }
        throw new SecurityException("You do not have permission to perform this operation");
    }

    public static void checkUserExist(User user) {
        if (user == null) {
            throw new IllegalArgumentException(USER_NOT_FOUND);
        }
    }


}
