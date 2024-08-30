package com.example.photocontest.repositories;

import com.example.photocontest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);
    User findByUsername(String username);
    User findById(int id);

    List<User> findListByUsername(String username);
}
