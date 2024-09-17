package com.example.photocontest.services;

import com.example.photocontest.exceptions.AuthorizationException;
import com.example.photocontest.exceptions.EntityNotFoundException;
import com.example.photocontest.filters.PhotoPostFilterOptions;
import com.example.photocontest.filters.specifications.PhotoPostSpecifications;
import com.example.photocontest.mappers.TagMapper;
import com.example.photocontest.models.PhotoPost;
import com.example.photocontest.models.Tag;
import com.example.photocontest.models.User;
import com.example.photocontest.repositories.PhotoPostRepository;
import com.example.photocontest.repositories.TagRepository;
import com.example.photocontest.repositories.UserRepository;
import com.example.photocontest.services.contracts.PhotoPostService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
public class PhotoPostServiceImpl implements PhotoPostService {

    private static final String MODIFY_POST_ERROR_MESSAGE = "Only post creator can modify a post.";

    private final PhotoPostRepository photoPostRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Autowired
    public PhotoPostServiceImpl(PhotoPostRepository photoPostRepository, UserRepository userRepository, TagRepository tagRepository, TagMapper tagMapper){
        this.photoPostRepository = photoPostRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public List<PhotoPost> getAllPhotoPosts() {
        return photoPostRepository.findAll();
    }

    @Override
    public PhotoPost getPhotoPostById(int id) {
        return photoPostRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post", id));
    }

    @Override
    public void createPhotoPost(PhotoPost photoPost) {
        photoPost.setCreator(getCurrentUser());
        photoPostRepository.save(photoPost);
    }


    @Override
    public void deletePhotoPost(int id) {
        checkUserPermissions(id);
        photoPostRepository.deleteById(id);
    }

    @Override
    public Page<PhotoPost> searchPhotoPosts(PhotoPostFilterOptions filterOptions, Pageable pageable) {
        Specification<PhotoPost> specification = Specification.where(null);

        if (filterOptions.getTitle() != null) {
            specification = specification.and(PhotoPostSpecifications.hasTitle(filterOptions.getTitle()));
        }

        if (filterOptions.getContest() != null) {
            specification = specification.and(PhotoPostSpecifications.belongsToContest(filterOptions.getContest()));
        }

        return photoPostRepository.findAll(specification, pageable);
    }

    @Override
    public List<PhotoPost> findByCreatedBy(User user) {
        return photoPostRepository.findByCreator(user);
    }

    @Override
    @Transactional
    public void addTag(int postId, int tagId) {
        checkUserPermissions(postId);
        PhotoPost post = photoPostRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("PhotoPost", postId));
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new EntityNotFoundException("Tag", tagId));
        post.getTags().add(tag);
        tag.getPosts().add(post);
        tagRepository.save(tag);
        photoPostRepository.save(post);
    }

    @Override
    public Page<PhotoPost> findAll(String usernameFilter, String emailFilter, String titleFilter, Pageable pageable) {
        Specification<PhotoPost> filters = Specification.where(
                        StringUtils.isEmptyOrWhitespace(usernameFilter) ? null : hasUsername(usernameFilter))
                .and(StringUtils.isEmptyOrWhitespace(emailFilter) ? null : hasEmail(emailFilter))
                .and(StringUtils.isEmptyOrWhitespace(titleFilter) ? null : hasTitle(titleFilter));

        return photoPostRepository.findAll(filters, pageable);
    }

    @Override
    public PhotoPost updatePhotoPost(PhotoPost photoPost) {
        return photoPostRepository.save(photoPost);
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepository.findByUsername(authentication.getName());
        return currentUser;
    }

    public void checkUserPermissions(int photoPostId) {
        User currentUser = getCurrentUser();
        PhotoPost photoPost = photoPostRepository.getPhotoPostById(photoPostId);
        if(!(photoPost.getCreator().equals(currentUser))){
            throw new AuthorizationException(MODIFY_POST_ERROR_MESSAGE);
        }
    }

    @Override
    public List<PhotoPost> getRecentPosts() {
        return photoPostRepository.findTop10ByOrderByUploadDateDesc();
    }

    @Override
    public List<PhotoPost> findByContest(int id) {
        return photoPostRepository.getPhotoPostsByContestId(id);
    }


    private Specification<PhotoPost> hasUsername(String username) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("createdBy").get("username")), "%" + username.toLowerCase() + "%");
    }

    private Specification<PhotoPost> hasEmail(String email) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("createdBy").get("email")), "%" + email.toLowerCase() + "%");
    }

    private Specification<PhotoPost> hasTitle(String title) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }
}
