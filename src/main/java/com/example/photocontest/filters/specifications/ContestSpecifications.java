package com.example.photocontest.filters.specifications;

import org.springframework.data.jpa.domain.Specification;
import com.example.photocontest.models.Contest;

public class ContestSpecifications {

    public static Specification<Contest> hasTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("title"), "%" + title + "%");
        };
    }

    public static Specification<Contest> hasCategory(String category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null || category.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    public static Specification<Contest> hasType(String type) {
        return (root, query, criteriaBuilder) -> {
            if (type == null || type.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    public static Specification<Contest> hasPhase(String phase) {
        return (root, query, criteriaBuilder) -> {
            if (phase == null || phase.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("phase"), phase);
        };
    }
}