package com.example.photocontest.filters.specifications;

import com.example.photocontest.filters.UserFilterOptions;
import org.springframework.data.jpa.domain.Specification;
import com.example.photocontest.models.User;

public class UserSpecifications {

    public static Specification<User> hasUsername(String username) {
        return (root, query, criteriaBuilder) ->
                username == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + username.toLowerCase() + "%");
    }

    public static Specification<User> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) ->
                firstName == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<User> hasLastName(String lastName) {
        return (root, query, criteriaBuilder) ->
                lastName == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<User> buildUserSpecification(UserFilterOptions filterOptions) {
        return Specification.where(hasUsername(filterOptions.getUsername()))
                .or(hasFirstName(filterOptions.getFirstName()))
                .or(hasLastName(filterOptions.getLastName()));
    }
}