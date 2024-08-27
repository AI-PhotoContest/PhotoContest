package com.example.photocontest.repositories;


import com.example.photocontest.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    Tag getById(int id);

    Optional<Tag> findByName(String name);
}
