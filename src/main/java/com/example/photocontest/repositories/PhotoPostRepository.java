package com.example.photocontest.repositories;

import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoPostRepository extends JpaRepository<PhotoPost, Integer>, JpaSpecificationExecutor<PhotoPost> {

    Page<PhotoPost> findAll(Specification<PhotoPost> filters, Pageable pageable);

    PhotoPost getPhotoPostById(int id);

    List<PhotoPost> findByCreator(User user);


    List<PhotoPost> findTop10ByOrderByUploadDateDesc();


//    List<PhotoPost> getPhotoPostByContestId(int contestId);
}
