package com.example.photocontest.controllers.mvc;

import com.example.photocontest.exceptions.EntityNotFoundException;
import com.example.photocontest.mappers.PhotoPostMapper;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.Tag;
import com.example.photocontest.models.dto.PhotoPostDto;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/photo-posts")
public class PhotoPostMvcController extends BaseController{

    private final PhotoPostService photoPostService;
    private final PhotoPostMapper mapper;
    private final UserService userService;
    private final PhotoPostMapper photoPostMapper;

    @Autowired
    public PhotoPostMvcController(PhotoPostService photoPostService, PhotoPostMapper mapper, UserService userService, PhotoPostMapper photoPostMapper) {

        super();
        this.photoPostService = photoPostService;
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

    @GetMapping("/create")
    public String showPostCreatePage(Model model) {
        model.addAttribute("post", new PhotoPostDto());
        return "post-pages/photo-post-create";
    }

    @PostMapping("/create")
    public String createPost(@Valid @ModelAttribute("post") PhotoPostDto photoPostDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "post-pages/photo-post-create";
        }
        photoPostDto.setTags(convertStringToTags(photoPostDto.getTagsInput()));

        try {
            PhotoPost post = photoPostMapper.toEntity(photoPostDto);
            photoPostService.createPhotoPost(post);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "post-pages/photo-post-create";
        }
    }

    private Set<Tag> convertStringToTags(String tagsInput) {
        return Arrays.stream(tagsInput.split(","))
                .map(String::trim)
                .map(tagName -> {
                    Tag tag = new Tag();
                    tag.setName(tagName);
                    // Optionally, you can look up existing tags in the database to avoid duplicates
                    return tag;
                })
                .collect(Collectors.toSet());
    }
}
