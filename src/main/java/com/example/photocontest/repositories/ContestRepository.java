package com.example.photocontest.repositories;

import com.example.photocontest.models.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ContestRepository extends JpaRepository<Contest, Integer>, JpaSpecificationExecutor<Contest> {

    Contest findById(int id);

}
