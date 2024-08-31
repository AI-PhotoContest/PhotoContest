package com.example.photocontest.services;

import com.example.photocontest.external.services.ImageGenerator;
import com.example.photocontest.models.Category;
import com.example.photocontest.repositories.CategoryRepository;
import com.example.photocontest.services.contracts.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(String name) {
        // Create a new category
        Category category = new Category();
        category.setName(name);

        // Generate an image for the category
        try {
            String generatedImageUrl = ImageGenerator.generateImageForCategory(name);
            category.setImage(generatedImageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed, e.g., set a default image URL or log an error
        }

        // Save the category in the database
        return categoryRepository.save(category);
    }
}