package com.example.photocontest.services;

import com.example.photocontest.exceptions.EntityDuplicateException;
import com.example.photocontest.exceptions.EntityNotFoundException;
import com.example.photocontest.filters.UserFilterOptions;
import com.example.photocontest.filters.specifications.UserSpecifications;
import com.example.photocontest.models.PointsSystem;
import com.example.photocontest.models.Role;
import com.example.photocontest.models.User;
import com.example.photocontest.models.enums.RoleType;
import com.example.photocontest.repositories.RoleRepository;
import com.example.photocontest.repositories.UserRepository;
import com.example.photocontest.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    //TODO if we have ranking we have to order them by rank

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<User> findAll(String usernameFilter, String emailFilter, String firstNameFilter, Pageable pageable) {
        return null;
    }

    public Page<User> searchUsers(UserFilterOptions filterOptions, Pageable pageable) {
        Specification<User> spec = UserSpecifications.buildUserSpecification(filterOptions);
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public User findUserById(int id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("User", id);
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User", "email" , email);
        }
        return user;
     }

    @Override
    public User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User", "username" , username);
        }
        return user;
    }

    @Override
    public boolean authenticateUser(String rawPassword, String storeHashedPassword) {
        return passwordEncoder.matches(rawPassword, storeHashedPassword);
    }

    @Override
    public User createUser(User user) {
        checkUsernameUnique(user);
        checkEmailUnique(user);
        RoleType photoJunkieType = RoleType.PHOTO_JUNKIE;
        Role role = roleRepository.findByName(photoJunkieType);
        user.setRoles(List.of(role));
        PointsSystem pointsSystem = new PointsSystem().InitializePoints();
        user.setPoints(pointsSystem);
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(int id) {
      User user =  userRepository.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("User", id);
        }
        userRepository.delete(user);

    }

    @Override
    public void createUserWithRole(User user, String role) {

    }

    @Override
    public User blockUser(User user) {
        user.setBlocked(true);
        return userRepository.save(user);
    }

    @Override
    public User unblockUser(User user) {
        user.setBlocked(false);
        return userRepository.save(user);
    }

    @Override
    public void uploadPhoto(User user, String photo) {

    }

    @Override
    public void updateUserProfilePhoto(String username, String fileName) {

    }

    @Override
    public void banUser(User user) {

    }

    @Override
    public void unbanUser(User user) {

    }

    @Override
    public User setRole(User user, String role) {

        Role roleToSet = roleRepository.findByName(RoleType.valueOf(role.toUpperCase()));
        if (roleToSet == null) {
            throw new EntityNotFoundException("Role", "role", role);
        }
        if (user.getRoles().contains(roleToSet)) {
            return user;
        }
        List<Role> roles = user.getRoles();
        roles.add(roleToSet);
        return userRepository.save(user);
    }

    @Override
    public User removeRole(User user, String role) {

        Role roleToRemove = roleRepository.findByName(RoleType.valueOf(role.toUpperCase()));
        if (roleToRemove == null) {
            throw new EntityNotFoundException("Role", "role", role);
        }
        if (!user.getRoles().contains(roleToRemove)) {
            return user;
        }
        List<Role> roles = user.getRoles();
        roles.remove(roleToRemove);
        return userRepository.save(user);
    }

    @Override
    public User makeUserVotable(User user) {
        user.setVotable(true);
        return userRepository.save(user);
    }

    @Override
    public User makeUserNotVotable(User user) {
        user.setVotable(false);
        return userRepository.save(user);
    }

    @Override
    public Role getRoleByName(String name) {
        return null;
    }

    @Override
    public void processOAuthPostLogin(String email, String Username) {

    }


    private void checkEmailUnique(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }
    }

    private void checkUsernameUnique(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
    }

}
