package com.example.photocontest.controllers.mvc;

import com.example.photocontest.mappers.PhotoPostMapper;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/photo-posts")
public class PhotoPostMvcController extends BaseController{

    private final PhotoPostService photoPostService;
    private final PhotoPostMapper mapper;
    private final UserService userService;

    @Autowired
    public PhotoPostMvcController(PhotoPostService photoPostService, PhotoPostMapper mapper, UserService userService) {

        this.photoPostService = photoPostService;
        this.mapper = mapper;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public String getPhotoPostById(@PathVariable int id) {
        PhotoPost photoPost = photoPostService.getPhotoPostById(id);

        return "photo-posts";
    }



}
