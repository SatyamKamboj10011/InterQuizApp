package com.satyam.FinalProjectBackend.models;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.ISBN;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String profile_picture;
    private String age;
    private String address;
    private String role;

    //FORGOT PASSWORD
    private String resetPasswordToken;
    private LocalDateTime passwordtokenExpiry;

    public User(Long id, LocalDateTime passwordtokenExpiry, String resetPasswordToken) {
        this.id = id;
        this.passwordtokenExpiry = passwordtokenExpiry;
        this.resetPasswordToken = resetPasswordToken;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public LocalDateTime getPasswordtokenExpiry() {
        return passwordtokenExpiry;
    }

    public void setPasswordtokenExpiry(LocalDateTime passwordtokenExpiry) {
        this.passwordtokenExpiry = passwordtokenExpiry;
    }

    @ElementCollection
    private Set<Long> likedQuizId = new HashSet<>();
    public User(){

    }

    public User(Set<Long> likedQuizId) {
        this.likedQuizId = likedQuizId;
    }

    public Set<Long> getLikedQuizId() {
        return likedQuizId;
    }

    public void setLikedQuizId(Set<Long> likedQuizId) {
        this.likedQuizId = likedQuizId;
    }

    public User(Long id, String username, String first_name, String last_name, String email, String password, String profile_picture, String age, String address, String role) {
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.profile_picture = profile_picture;
        this.age = age;
        this.address = address;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
