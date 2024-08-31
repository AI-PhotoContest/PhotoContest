package com.example.photocontest.repositories;

import com.example.photocontest.models.Contest;
import com.example.photocontest.models.PhotoPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Integer>, JpaSpecificationExecutor<Contest> {

    Contest findById(int id);

    @Query("SELECT p FROM PhotoPost p WHERE p.contest.id = :contestId")
    List<PhotoPost> findAllByContestId(@Param("contestId") int contestId);

}
