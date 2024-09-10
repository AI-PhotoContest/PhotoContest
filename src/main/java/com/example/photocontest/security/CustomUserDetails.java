package com.example.photocontest.security;

import com.example.photocontest.models.Role;
import com.example.photocontest.models.User;
import com.example.photocontest.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<User> user = userRepository.findListByEmail(email);
        if (user.size() == 0) {
            throw new UsernameNotFoundException("User details not found for the user : " + email);
        }
        return new org.springframework.security.core.userdetails.User(
                user.get(0).getUsername(),
                user.get(0).getPassword(),
                user.get(0).getAuthorities()
        );
    }
}