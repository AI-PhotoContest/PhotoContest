package com.example.photocontest.controllers.mvc;

import com.example.photocontest.mappers.PhotoPostMapper;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.repositories.TagRepository;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/photo-posts")
public class PhotoPostMvcController extends BaseController {

    private final PhotoPostService photoPostService;


    @Autowired
    public PhotoPostMvcController(PhotoPostService photoPostService) {
        super();
        this.photoPostService = photoPostService;
    }

    @GetMapping
    public String listPhotoPosts(Model model) {
        List<PhotoPost> posts = photoPostService.getAllPhotoPosts();
        model.addAttribute("active", "posts");
        model.addAttribute("posts", posts);
        return "post-pages/photo-posts";
    }

    @GetMapping("/{id}")
    public String getPhotoPosts(@PathVariable int id,Model model) {
        PhotoPost photoPost = photoPostService.getPhotoPostById(id);
        model.addAttribute("post", photoPost);
        return "post-pages/photo-post-page";
    }

}
