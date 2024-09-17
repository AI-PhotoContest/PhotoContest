package com.example.photocontest.controllers.mvc;

import com.example.photocontest.exceptions.EntityNotFoundException;
import com.example.photocontest.mappers.PhotoPostMapper;
import com.example.photocontest.models.Contest;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.Tag;
import com.example.photocontest.models.User;
import com.example.photocontest.models.dto.PhotoPostDto;
import com.example.photocontest.repositories.TagRepository;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.photocontest.helpers.AuthenticationHelpers.checkPermission;
import static com.example.photocontest.helpers.AuthenticationHelpers.extractUserFromProvider;

@Controller
@RequestMapping("/photo-posts")
public class PhotoPostMvcController extends BaseController {

    private final PhotoPostService photoPostService;
    private final TagRepository tagRepository;
    private final PhotoPostMapper mapper;
    private final UserService userService;
    private final PhotoPostMapper photoPostMapper;

    @Autowired
    public PhotoPostMvcController(PhotoPostService photoPostService, TagRepository tagRepository, PhotoPostMapper mapper, UserService userService, PhotoPostMapper photoPostMapper) {

        super();
        this.photoPostService = photoPostService;
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.userService = userService;
        this.photoPostMapper = photoPostMapper;
    }

    @GetMapping
    public String listPhotoPosts(Model model) {
        List<PhotoPost> posts = photoPostService.getAllPhotoPosts();
        model.addAttribute("active", "posts");
        model.addAttribute("posts", posts);
        return "post-pages/photo-posts";
    }

    @GetMapping("/{id}")
    public String getPhotoPostById(@PathVariable int id) {
        PhotoPost photoPost = photoPostService.getPhotoPostById(id);
        return "post-pages/photo-posts";
    }

}
