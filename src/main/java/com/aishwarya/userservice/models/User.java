package com.aishwarya.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "users")
public class User extends BaseModel {
    private String name;
    private String email;
    private String password;
    @ManyToMany
    private List<Role> roles;
}

// User Roles
// 1 M
// 1 M
// M: M
