package com.example.photocontest.controllers.mvc;


import com.example.photocontest.mappers.UserMapper;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/auth")
public class AuthenticationController extends BaseController {

    private final UserMapper mapper;

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserMapper mapper, UserService userService) {
        this.mapper = mapper;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("active", "login");
        return "login";
    }

}
