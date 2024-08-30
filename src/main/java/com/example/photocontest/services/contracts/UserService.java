package com.example.photocontest.services.contracts;

import com.example.photocontest.models.Role;
import com.example.photocontest.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<User> findAll(String usernameFilter, String emailFilter, String firstNameFilter, Pageable pageable);
    List<User> getAll();
    User findUserById(int id);
    User findUserByEmail(String email);
    User findUserByUsername(String username);
    boolean authenticateUser(String rawPassword, String storeHashedPassword);

    User createUser(User user);
    User updateUser(User user);
    void deleteUser(int id);

    void createUserWithRole(User user, String role);

    User makeUserVotable(User user);
    User makeUserNotVotable(User user);

    User blockUser(User user);

    User unblockUser(User user);

    void uploadPhoto(User user, String photo);

    void updateUserProfilePhoto(String username, String fileName);

    void banUser(User user);

    void unbanUser(User user);


    Role getRoleByName(String name);

    void processOAuthPostLogin(String email,String Username);

    User setRole(User user, String role);
    User removeRole(User user, String role);
}
