package com.example.photocontest.models.dto;

import com.example.photocontest.models.Tag;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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

    @NotEmpty(message = "Photo cannot be empty!")
    private String photo;

    private Set<Tag> tags;

    private String tagsInput;
}
