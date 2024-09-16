package com.example.photocontest.repositories;

import com.example.photocontest.models.Category;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

  Category findByName(String name);

  @NotNull List<Category> findAll();

}
