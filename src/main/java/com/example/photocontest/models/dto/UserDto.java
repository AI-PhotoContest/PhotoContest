package com.example.photocontest.models.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    @NotEmpty
    private String username;

    @NotEmpty(message = "First name cannot be empty!")
    @Size(min = 4, max = 32, message = "First name must be between 4 and 32 characters long!")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty!")
    @Size(min = 4, max = 32, message = "Last name must be between 4 and 32 characters long!")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty!")
    @Email(message = "Email must be a valid email address!")
    private String email;

    @NotEmpty
    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{4,12}$",
            message = "Password must contain one digit from 1 to 9, " +
                    "one lowercase letter, " +
                    "one uppercase letter, " +
                    "one special character, " +
                    "no space, and " +
                    "it must be 4-12 characters long.")
    private String password;

//    @NotEmpty
//    @NotNull
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{4,12}$",
//            message = "Password must contain one digit from 1 to 9, " +
//                    "one lowercase letter, " +
//                    "one uppercase letter, " +
//                    "one special character, " +
//                    "no space, and " +
//                    "it must be 4-12 characters long.")
//    private String confirmPassword;


}
