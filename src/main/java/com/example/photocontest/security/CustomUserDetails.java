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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Намиране на потребителя по име (или email)
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        // Преобразуване на ролите в GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }

        // Създаване на UserDetails обект
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername()) // използвайте `user.getUsername()` вместо `user.getEmail()`, ако потребителят се логва с потребителско име
                .password(user.getPassword())
                .authorities(authorities)
                .accountLocked(user.isBlocked()) // Проверка дали акаунтът е блокиран
                .disabled(user.isBanned()) // Проверка дали акаунтът е забранен
                .build();
    }
}