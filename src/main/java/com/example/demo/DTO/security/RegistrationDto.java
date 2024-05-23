package com.example.demo.DTO.security;

import com.example.demo.models.security.RoleEntity;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class RegistrationDto {
    private Long id;
    @NotEmpty(message ="Must be not empty")
    private String username;
    @NotEmpty(message ="Must be not empty")
    private String email;
    @NotEmpty(message ="Must be not empty")
    private String password;
    private String town;
    private Long phoneNumber;
    List<RoleEntity> roles;
}
