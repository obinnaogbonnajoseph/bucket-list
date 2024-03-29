package com.obinna.bucketlist.dto;

import com.obinna.bucketlist.model.Role;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class UserDto {

    private String firstName;

    private String lastName;

    @NotBlank
    private String username;

    private String email;

    @NotBlank
    private String password;

    private List<Role> roles;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() { return roles; }

    public void setRoles(List<Role> roles) { this.roles = roles; }
}
