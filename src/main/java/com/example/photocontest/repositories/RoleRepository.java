package com.example.photocontest.repositories;

import com.example.photocontest.models.Role;
import com.example.photocontest.models.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findById(int id);

    Role findByName(RoleType name);

}
