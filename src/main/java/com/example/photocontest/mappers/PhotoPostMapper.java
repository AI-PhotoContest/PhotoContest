package com.example.photocontest.mappers;

import com.example.photocontest.models.CameraDetails;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.Tag;
import com.example.photocontest.models.dto.PhotoPostDto;
import com.example.photocontest.services.contracts.PhotoPostService;
import com.example.photocontest.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PhotoPostMapper {

    private final PhotoPostService postService;
    private final TagService tagService;

    @Autowired
    public PhotoPostMapper(PhotoPostService postService, TagService tagService) {

        this.postService = postService;
        this.tagService = tagService;
    }

    public PhotoPostDto toDto(PhotoPost photoPost) {
        PhotoPostDto dto = new PhotoPostDto();
        dto.setTitle(photoPost.getTitle());
        dto.setDescription(photoPost.getDescription());
        dto.setPhoto(photoPost.getImage());
       // dto.setTags(photoPost.getTags().stream().map(Tag::getName).collect(Collectors.toSet())); // Преобразуване на таговете в Set от String
        dto.setCameraModel(photoPost.getCameraDetails().getCameraModel());
        dto.setLensMake(photoPost.getCameraDetails().getLensMake());
        dto.setLensModel(photoPost.getCameraDetails().getLensModel());
        dto.setShutterSpeed(photoPost.getCameraDetails().getShutterSpeed());
        dto.setAperture(photoPost.getCameraDetails().getAperture());
        dto.setFocalLength(photoPost.getCameraDetails().getFocalLength());
        dto.setIso(photoPost.getCameraDetails().getIso());
        dto.setLocation(photoPost.getLocation());
        dto.setRetouchingApplied(photoPost.getRetouchingApplied());
        dto.setTakenDate(photoPost.getTakenDate());
        return dto;
    }

    public PhotoPost toEntity(PhotoPostDto dto) {
        PhotoPost photoPost = new PhotoPost();
        photoPost.setTitle(dto.getTitle());
        photoPost.setDescription(dto.getDescription());
        photoPost.setImage(dto.getPhoto());
        Set<Tag> tags = tagService.findOrCreateTags(dto.getTags());
        photoPost.setTags(tags);
        photoPost.setCameraDetails(mapCameraDetails(dto, photoPost));
        photoPost.setLocation(dto.getLocation());
        photoPost.setRetouchingApplied(dto.getRetouchingApplied());
        photoPost.setTakenDate(dto.getTakenDate());
        photoPost.setUploadDate(LocalDateTime.now());
        return photoPost;
    }

    private Set<Tag> mapTags(Set<String> tagsDto) {
        if (tagsDto == null) {
            return null;
        }
        return tagsDto.stream().map(tagName -> {
            Tag tag = new Tag();
            tag.setName(tagName);
            return tag;
        }).collect(Collectors.toSet());
    }

    private CameraDetails mapCameraDetails(PhotoPostDto dto, PhotoPost photoPost) {
        CameraDetails cameraDetails = new CameraDetails();
        cameraDetails.setCameraModel(dto.getCameraModel());
        cameraDetails.setLensMake(dto.getLensMake());
        cameraDetails.setLensModel(dto.getLensModel());
        cameraDetails.setShutterSpeed(dto.getShutterSpeed());
        cameraDetails.setAperture(dto.getAperture());
        cameraDetails.setFocalLength(dto.getFocalLength());
        cameraDetails.setIso(dto.getIso());
//        cameraDetails.setPhotoPost(photoPost);  // Свързване на CameraDetails с PhotoPost
        return cameraDetails;
    }
}