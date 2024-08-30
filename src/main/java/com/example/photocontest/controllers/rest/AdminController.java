package com.example.photocontest.controllers.rest;

import com.example.photocontest.models.User;
import com.example.photocontest.repositories.UserRepository;
import com.example.photocontest.services.contracts.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.example.photocontest.helpers.AuthenticationHelpers.checkPermission;
import static com.example.photocontest.helpers.AuthenticationHelpers.checkUserExist;


@RestController
@RequestMapping("/api/users")
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @PostMapping("/admin/roles/{id}")
    public User setRole(@PathVariable int id, @RequestParam String role, Principal principal) {
        checkPermission(principal, "ADMIN");
        User user = userRepository.findById(id);
        checkUserExist(user);
        return userService.setRole(user, role);
    }

    @DeleteMapping("/admin/roles/{id}")
    public User removeRole(@PathVariable int id, @RequestParam String role, Principal principal) {
        checkPermission(principal, "ADMIN");
        User user = userRepository.findById(id);
        checkUserExist(user);
        return userService.removeRole(user, role);
    }

    @PutMapping("/admin/block/{id}")
    @Operation(summary = "Block user", description = "Only Admin user can block user")
    public User blockUser(@PathVariable int id, Principal principal) {
        checkPermission(principal, "ADMIN");
        User userToBlock = userService.findUserById(id);
        checkUserExist(userToBlock);
        return userService.blockUser(userToBlock);
    }

    @PutMapping("/admin/unblock/{id}")
    @Operation(summary = "Unblock user", description = "Only Admin user can unblock user")
    public User unblockUser(@PathVariable int id, Principal principal) {
        checkPermission(principal, "ADMIN");
        User userToUnblock = userService.findUserById(id);
        checkUserExist(userToUnblock);
        return userService.unblockUser(userToUnblock);
    }

    @PutMapping("/admin/votable/{id}")
    @Operation(summary = "Make user votable", description = "Only Admin user can make user votable")
    public User makeUserVotable(@PathVariable int id, Principal principal) {
        checkPermission(principal, "ADMIN");
        User userToMakeVotable = userService.findUserById(id);
        checkUserExist(userToMakeVotable);
        return userService.makeUserVotable(userToMakeVotable);
    }

    @PutMapping("/admin/unvotable/{id}")
    @Operation(summary = "Make user unvotable", description = "Only Admin user can make user un-votable")
    public User makeUserNotvotable(@PathVariable int id, Principal principal) {
        checkPermission(principal, "ADMIN");
        User userToMakeNotVotable = userService.findUserById(id);
        checkUserExist(userToMakeNotVotable);
        return userService.makeUserNotVotable(userToMakeNotVotable);
    }

}
