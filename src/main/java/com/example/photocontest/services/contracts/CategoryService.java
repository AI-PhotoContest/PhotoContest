package com.example.photocontest.services.contracts;

import com.example.photocontest.models.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(String name);

    List<Category> getAllCategories();
}
