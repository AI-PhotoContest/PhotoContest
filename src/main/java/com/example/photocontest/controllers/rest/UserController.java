package com.example.photocontest.controllers.rest;

import com.example.photocontest.mappers.UserMapper;
import com.example.photocontest.models.dto.UserDto;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public void getAllUsers() {
        userService.getAll();
    }

    @GetMapping("/{id}")
    public void getUserById(@PathVariable int id) {
        userService.findUserById(id);
    }

    @PostMapping
    public void createUser(@RequestBody UserDto userDto) {
        userService.createUser(userMapper.fromDto(userDto));
    }

}
