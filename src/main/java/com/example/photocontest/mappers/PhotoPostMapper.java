package com.example.photocontest.mappers;

import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.Tag;
import com.example.photocontest.models.dto.PhotoPostDto;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PhotoPostMapper {

    private final PhotoPostService photoPostService;
    private final TagService tagService;

    @Autowired
    public PhotoPostMapper(PhotoPostService photoPostService, TagService tagService) {
        this.photoPostService = photoPostService;
        this.tagService = tagService;
    }

    public PhotoPost fromDto(int id, PhotoPostDto postDto) {
        PhotoPost post = fromDto(postDto);
        post.setId(id);
        PhotoPost repositoryPost = photoPostService.getPhotoPostById(id);
        post.setCreator(repositoryPost.getCreator());
        return post;
    }

    public PhotoPost fromDto(PhotoPostDto photoPostDto) {
        PhotoPost photoPost = new PhotoPost();
        photoPost.setTitle(photoPostDto.getTitle());
        photoPost.setDescription(photoPostDto.getDescription());
        photoPost.setImage(photoPostDto.getPhoto());
        Set<Tag> tags = tagService.findOrCreateTags(photoPostDto.getTags());
        photoPost.setTags(tags);
        return photoPost;
    }
}
