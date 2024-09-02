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

import java.security.Principal;
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


    /**
     * Creates a new PhotoPost based on the provided PhotoPostDto.
     * <p>
     * This method accepts a JSON body containing the details of a photo post, including title, description, photo URL,
     * tags, camera details, location, retouching information, and the date the photo was taken. The method validates the
     * incoming data and, if valid, converts it into a PhotoPost entity, saves it, and returns the saved entity as a response.
     * * Example Postman request:
     *      * <p>
     *      * ```
     *      * POST http://localhost:8080/api/photo-posts
     *      * ```
     *      * <p>
     * <p>
     * Example usage with Postman:
     * <p>
     * To create a new PhotoPost, you need to provide the following fields:
     * - `title` (required): A descriptive title for the photo post, between 3 and 64 characters long.
     * - `description` (required): A detailed description of the photo, between 32 and 8192 characters long.
     * - `photo` (required): A URL or path to the photo file.
     * - `tags` (optional): A set of tags that categorize the photo (e.g., `nature`, `portrait`).
     * - `cameraDetails` (optional): Details about the camera and lens used to take the photo, such as camera model, lens model, etc.
     * - `location` (optional): The location where the photo was taken.
     * - `retouchingApplied` (optional): Information about any retouching or editing applied to the photo.
     * - `takenDate` (optional): The date when the photo was taken.
     * <p>
     * Example JSON request body:
     * <pre>
     * {
     *   "title": "Sunset at the Beach",
     *   "description": "A beautiful sunset captured at the beach with vibrant colors and peaceful waves.",
     *   "photo": "https://example.com/photos/sunset-beach.jpg",
     *   "tags": ["sunset", "beach", "nature"],
     *   "cameraDetails": {
     *     "cameraModel": "Canon EOS 5D Mark IV",
     *     "lensMake": "Canon",
     *     "lensModel": "EF 24-70mm f/2.8L II USM",
     *     "shutterSpeed": "1/200",
     *     "aperture": "f/8.0",
     *     "focalLength": "70mm",
     *     "iso": "100"
     *   },
     *   "location": "Malibu, CA",
     *   "retouchingApplied": "Minimal retouching in Lightroom to enhance colors.",
     *   "takenDate": "2023-08-15"
     * }
     * </pre>
     * <p>
     * The method returns a 201 Created status upon successful creation of the PhotoPost, along with the created
     * PhotoPost's details. If there are any validation errors, the method returns appropriate HTTP status codes
     * such as 400 Bad Request.
     *
     * @param photoPostDto The DTO containing the photo post details.
     * @return The created PhotoPost entity wrapped in a ResponseEntity with a 201 Created status.
     */
    @PostMapping
    public PhotoPost create(@Valid @RequestBody PhotoPostDto photoPostDto, Principal principal) {

            PhotoPost photoPost = mapper.toEntity(photoPostDto);
            User user = userService.findUserByUsername(principal.getName());
            photoPost.setCreator(user);

            photoPostService.createPhotoPost(photoPost);
            return photoPost;
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
