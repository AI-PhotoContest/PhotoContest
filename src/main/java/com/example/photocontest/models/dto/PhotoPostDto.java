package com.example.photocontest.models.dto;

import com.example.photocontest.models.CameraDetails;
import com.example.photocontest.models.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class PhotoPostDto {

    @NotEmpty(message = "Title cannot be empty!")
    @Size(min = 3, max = 64, message = "Title must be between 16 and 64 characters long!")
    private String title;

    @NotEmpty(message = "Content cannot be empty!")
    @Size(min = 32, max = 8192 , message = "Content must be between 32 and 8192 characters long!")
    private String description;

//    @NotEmpty(message = "Photo cannot be empty!")
    private String photo;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate takenDate;

    private Set<Tag> tags; // вместо Set<Tag>
    private String tagsInput;

    private String cameraModel;
    private String lensMake;
    private String lensModel;
    private String shutterSpeed;
    private String aperture;
    private String focalLength;
    private String iso;

    private String location;

    private String retouchingApplied;


}
