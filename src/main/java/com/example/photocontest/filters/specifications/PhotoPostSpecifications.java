package com.example.photocontest.filters.specifications;

import com.example.photocontest.models.Contest;
import com.example.photocontest.models.PhotoPost;
import org.springframework.data.jpa.domain.Specification;

public class PhotoPostSpecifications {
    public static Specification<PhotoPost> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("title"), "%" + title + "%");
        };
    }

    public static Specification<PhotoPost> belongsToContest(Contest contest) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("contest"), contest);
    }
}