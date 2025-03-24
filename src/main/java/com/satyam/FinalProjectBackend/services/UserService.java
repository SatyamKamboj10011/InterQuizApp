package com.satyam.FinalProjectBackend.services;

import com.satyam.FinalProjectBackend.db.UserRepo;
import com.satyam.FinalProjectBackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User registeredUser(User user){
        return userRepo.save(user);
    }
    public Optional<User> getUserByUsername(String username){
        return userRepo.findByUsername(username);
    }
}
