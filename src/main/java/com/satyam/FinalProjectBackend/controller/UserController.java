package com.satyam.FinalProjectBackend.controller;

import com.satyam.FinalProjectBackend.models.User;
import com.satyam.FinalProjectBackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public User registerNewUser(@RequestBody User user){
        return userService.registeredUser(user);
    }
    @GetMapping("/{username}")
    public Optional<User> getUser(@PathVariable String username){
        return userService.getUserByUsername(username);
    }
}
