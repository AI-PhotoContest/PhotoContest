package com.example.photocontest.controllers.rest;

import com.example.photocontest.exceptions.AuthorizationException;
import com.example.photocontest.exceptions.EntityDuplicateException;
import com.example.photocontest.exceptions.EntityNotFoundException;
import com.example.photocontest.mappers.PhotoPostMapper;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import com.example.photocontest.models.dto.PhotoPostDto;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/photo-posts")
@Tag(name = "PhotoPosts", description = "Operations for managing photo posts")
public class PhotoPostController {

    private final PhotoPostService photoPostService;
    private final PhotoPostMapper mapper;
    private final UserService userService;

    @Autowired
    public PhotoPostController(PhotoPostService photoPostService, PhotoPostMapper mapper, UserService userService) {

        this.photoPostService = photoPostService;
        this.mapper = mapper;
        this.userService = userService;
    }

    // Get all posts
    @GetMapping
    public List<PhotoPost> getAll() {
        return photoPostService.getAllPhotoPosts();
    }

    // Get post by ID
    @GetMapping("/{id}")
    public PhotoPost getById(@PathVariable int id) {
        try {
            return photoPostService.getPhotoPostById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/users/{id}")
    public List<PhotoPost> getUserPosts(@PathVariable int id) {
        try {
            User user = userService.findUserById(id);
            return photoPostService.findByCreatedBy(user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //Create a new photo post
    @PostMapping
    public PhotoPost create(@Valid @RequestBody PhotoPostDto photoPostDto) {
        try {
            PhotoPost photoPost = mapper.fromDto(photoPostDto);
            photoPostService.createPhotoPost(photoPost);
            return photoPost;
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    // Delete a post
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        try {
            photoPostService.deletePhotoPost(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{postId}/tag/{tagId}")
    public void addTagToPost(@PathVariable int postId, @PathVariable int tagId) {
        try{
            photoPostService.addTag(postId,tagId);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (EntityDuplicateException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
}
