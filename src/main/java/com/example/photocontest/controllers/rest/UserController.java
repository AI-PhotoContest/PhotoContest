package com.example.photocontest.controllers.rest;

import com.example.photocontest.filters.UserFilterOptions;
import com.example.photocontest.mappers.UserMapper;
import com.example.photocontest.models.Role;
import com.example.photocontest.models.User;
import com.example.photocontest.models.dto.UserDto;
import com.example.photocontest.repositories.UserRepository;
import com.example.photocontest.services.EmailService;
import com.example.photocontest.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, UserMapper userMapper, EmailService emailService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.emailService = emailService;
    }


    /**
     * This controller handles user-related operations such as listing and searching for users.
     * <p>
     * Example usage with Postman:
     * <p>
     * To search for users by username, first name, or last name, use the following query parameters:
     * <p>
     * - `username` (optional): The username to search for (e.g., `pesho`).
     * <p>
     * - `firstName` (optional): The first name to search for (e.g., `Petar`).
     * <p>
     * - `lastName` (optional): The last name to search for (e.g., `Petrov`).
     * <p>
     * - `page` (optional): The page number to retrieve (e.g., `0` for the first page).
     * <p>
     * - `size` (optional): The number of users per page (e.g., `10`).
     * <p>
     * - `sort` (optional): The sorting criteria (e.g., `username,asc` for ascending order by username).
     * <p>
     * Example Postman request:
     * <p>
     * <a href="http://localhost:8080/users?username=pesho&firstName=Petar&page=0&size=10&sort=username,asc">
     * http://localhost:8080/users?username=pesho&firstName=Petar&page=0&size=10&sort=username,asc</a>
     * <p>
     * Click the link above or paste it into your browser or Postman to execute the request.
     * <p>
     *
     *
     * This request will search for users with the username "pesho" and first name "Petar", returning
     * results on the first page with 10 users per page, sorted by username in ascending order.
     */

    @GetMapping
    public Page<User> getAllUsers(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            Pageable pageable) {
        UserFilterOptions filterOptions = new UserFilterOptions();
        filterOptions.setUsername(username);
        filterOptions.setFirstName(firstName);
        filterOptions.setLastName(lastName);
        return userService.searchUsers(filterOptions, pageable);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
       return userService.findUserById(id);
    }

    @PostMapping
    public User registerUser(@Valid @RequestBody UserDto userDto) {
        User user = userService.createUser(userMapper.fromDto(userDto));
        emailService.sendSimpleEmail(user.getEmail(),user.getUsername());
        return user;
    }

    @GetMapping("/{id}/roles")
    public List<Role> getUserRoles(@PathVariable int id) {
        return userService.findUserById(id).getRoles();
    }



}
