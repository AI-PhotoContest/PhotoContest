/* DO NOT DELETE THIS FILE
 * This file is used to initialize the roles in the database
 * The roles are initialized in the database when the application starts
 * The RoleRepository is autowired to the DataInitializer class
 */

package com.example.photocontest.services;

import com.example.photocontest.models.*;
import com.example.photocontest.models.enums.*;
import com.example.photocontest.repositories.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final ContestRepository contestRepository;
    private final PhotoPostRepository photoPostRepository;
    private final UserRepository userRepository;

    @Autowired
    public DataInitializer(RoleRepository roleRepository,
                           ContestRepository contestRepository,
                           PhotoPostRepository photoPostRepository,
                           UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.contestRepository = contestRepository;
        this.photoPostRepository = photoPostRepository;
        this.userRepository = userRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        initializeRoles();
        initializeContestsAndPhotoPosts();
    }

    private void initializeRoles() {
        for (RoleType roleType : RoleType.values()) {
            if (roleRepository.findByName(roleType) == null) {
                Role role = new Role();
                role.setName(roleType);
                roleRepository.save(role);
            }
        }
    }

    private void initializeContestsAndPhotoPosts() {
        User creator = userRepository.findById(1);
        if (creator == null) {
            System.out.println("No users found in the database. Skipping contests and photo posts initialization.");
            return;
        }

        Contest contest1 = new Contest();
        contest1.setTitle("Summer Photography Contest");
        contest1.setDescription("Capture the essence of summer in a photo!");
        contest1.setPhotoUrl("Elephant Dust Bath, Africa, 2000.jpg");
        contest1.setStatus(ContestStatus.OPEN);
        contest1.setPhase(ContestPhase.PHASE1);
        contest1.setCreator(creator);
        contest1.setStartDate(LocalDateTime.now());
        contest1.setEndDate(LocalDateTime.now().plusDays(30));

        Contest contest2 = new Contest();
        contest2.setTitle("Winter Wonderland Contest");
        contest2.setDescription("Show us your best winter shots!");
        contest2.setPhotoUrl("1730641926750_Firefly peanut man happy 3d 98254.jpg");
        contest2.setStatus(ContestStatus.OPEN);
        contest2.setPhase(ContestPhase.PHASE1);
        contest2.setCreator(creator);
        contest2.setStartDate(LocalDateTime.now());
        contest2.setEndDate(LocalDateTime.now().plusDays(30));

        contestRepository.saveAll(List.of(contest1, contest2));

        PhotoPost photoPost1 = new PhotoPost();
        photoPost1.setTitle("Beach Sunset");
        photoPost1.setDescription("A beautiful sunset at the beach.");
        photoPost1.setImage("Elephant Dust Bath, Africa, 2000.jpg");
        photoPost1.setContest(contest1);

        PhotoPost photoPost2 = new PhotoPost();
        photoPost2.setTitle("Snowy Mountains");
        photoPost2.setDescription("Capturing the majestic mountains covered in snow.");
        photoPost2.setImage("Flamingos in Flight.jpg");
        photoPost2.setContest(contest2);

        photoPostRepository.saveAll(List.of(photoPost1, photoPost2));

        System.out.println("Roles, contests, and photo posts initialized in the database.");
    }
}