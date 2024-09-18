package com.example.photocontest.repositories;

import com.example.photocontest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);
    User findByUsername(String username);
    User findById(int id);

    List<User> findListByUsername(String username);
    //fetch only users that are votable=true
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.isVotable = true")
    User findVotableUserByUsername(@Param("username") String username);

    List<User> findListByEmail(String email);

    List<User> findByUsernameContainingIgnoreCase(String username);
}
