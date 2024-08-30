package com.example.photocontest.repositories;

import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoPostRepository extends JpaRepository<PhotoPost, Integer> {

    Page<PhotoPost> findAll(Specification<PhotoPost> filters, Pageable pageable);

    PhotoPost getPhotoPostById(int id);

    List<PhotoPost> findByCreatedBy(User user);

    List<PhotoPost> getPhotoPostByContestId(int contestId);
}
