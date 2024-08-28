/* DO NOT DELETE THIS FILE
    * This file is used to initialize the roles in the database
    * The roles are initialized in the database when the application starts
    * The RoleRepository is autowired to the DataInitializer class
 */


package com.example.photocontest.services;

import com.example.photocontest.models.Role;
import com.example.photocontest.models.enums.RoleType;
import com.example.photocontest.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;

    @Autowired
    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeRoles() {
        for (RoleType roleType : RoleType.values()) {
            if (roleRepository.findByName(roleType) == null) {
                Role role = new Role();
                role.setName(roleType);
                roleRepository.save(role);
            }
        }
    }
}