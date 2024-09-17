package com.example.photocontest.services.contracts;

import com.example.photocontest.filters.PhotoPostFilterOptions;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PhotoPostService  {

    List<PhotoPost> getAllPhotoPosts();

    PhotoPost getPhotoPostById(int id);

    void createPhotoPost(PhotoPost photoPost);

    void deletePhotoPost(int id);

    Page<PhotoPost> searchPhotoPosts(PhotoPostFilterOptions filterOptions, Pageable pageable);

    List<PhotoPost> findByCreatedBy(User user);

    void addTag(int postId, int tagId);

    Page<PhotoPost> findAll(String usernameFilter, String emailFilter, String titleFilter, Pageable pageable);

    PhotoPost updatePhotoPost(PhotoPost photoPost);

    List<PhotoPost> getRecentPosts();

    List<PhotoPost> findByContest(int id);

    void ratePhotoPost(int postId, int score, User judge);
}
