package com.example.photocontest.models.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDto {

    private int photoPostId;

    @Min(value = 0, message = "Score must be between 0 and 10")
    @Max(value = 10, message = "Score must be between 0 and 10")
    @NotNull(message = "Score is required")
    private Integer score;

    @NotBlank(message = "Comment is required")
    private String comment;

    private boolean categoryMismatch;

    public void setCategoryMismatch(boolean categoryMismatch) {
        this.categoryMismatch = categoryMismatch;
        if (categoryMismatch) {
            this.score = 0; // Ако категорията не съвпада, оценката е 0
            this.comment = "The photo does not fit the contest category."; // Съобщение по подразбиране
        }
    }
}