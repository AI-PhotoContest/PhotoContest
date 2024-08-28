package com.example.photocontest.mappers;

import com.example.photocontest.exceptions.EntityNotFoundException;
import com.example.photocontest.external.services.EmailValidator;
import com.example.photocontest.models.User;
import com.example.photocontest.models.dto.UserDto;
import com.example.photocontest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private UserRepository repository;

    public User fromDto(UserDto dto, int id) {
        User repositoryUser = repository.findById(id);
        if (repositoryUser == null) {
            throw new EntityNotFoundException("User", id);
        }
        User user = fromDto(dto);
        user.setUsername(repositoryUser.getUsername());
        user.setRoles(repositoryUser.getRoles());
        user.setId(id);
        return user;
    }

    public User fromDto(UserDto dto) {
        if (!EmailValidator.isEmailValid(dto.getEmail())) {
            throw new IllegalArgumentException("Invalid email");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        return user;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPassword(user.getPassword());
        return userDto;
    }

    public boolean checkPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

}
