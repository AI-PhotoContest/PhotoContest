package com.example.photocontest.controllers.rest;

import com.example.photocontest.mappers.UserMapper;
import com.example.photocontest.models.User;
import com.example.photocontest.models.dto.UserDto;
import com.example.photocontest.services.EmailService;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper, EmailService emailService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.emailService = emailService;
    }

    @GetMapping
    public List<User> getAllUsers() {
      return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
       return userService.findUserById(id);
    }

    @PostMapping
    public User createUser(@RequestBody UserDto userDto) {
        User user = userService.createUser(userMapper.fromDto(userDto));
        emailService.sendSimpleEmail(user.getEmail(),user.getUsername());
        return user;
    }
}
