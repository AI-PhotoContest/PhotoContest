package com.example.photocontest.repositories;

import com.example.photocontest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByUsername(String username);
    User findById(int id);
    List<User> findAll();

}
