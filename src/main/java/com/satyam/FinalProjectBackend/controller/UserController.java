package com.satyam.FinalProjectBackend.controller;

import com.satyam.FinalProjectBackend.db.UserRepo;
import com.satyam.FinalProjectBackend.models.LoginRequest;
import com.satyam.FinalProjectBackend.models.User;
import com.satyam.FinalProjectBackend.services.EmailService;
import com.satyam.FinalProjectBackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    @PostMapping("/registration")
    public User registerNewUser(@RequestBody User user){
        return userService.registeredUser(user);
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setFirst_name(updatedUser.getFirst_name());
            user.setLast_name(updatedUser.getLast_name());
            user.setEmail(updatedUser.getEmail());
            user.setAge(updatedUser.getAge());
            user.setAddress(updatedUser.getAddress());
            user.setProfile_picture(updatedUser.getProfile_picture());
            userRepo.save(user);
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }


    @GetMapping("/validate/{username}")
    public ResponseEntity<?> validateUser(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepo.findByEmail(loginRequest.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // 🔐 Check if the raw password matches the encoded password
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.ok(user); // Send user details
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    //LIKE UNLIKE FEATURE
    @PostMapping("/{userId}/likes/{quizId}")
    public ResponseEntity<?> likeQuiz(@PathVariable Long userId, @PathVariable Long quizId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        user.getLikedQuizId().add(quizId);
        userRepo.save(user);
        return ResponseEntity.ok("Quiz liked");
    }

    @GetMapping("/{userId}/likes")
    public ResponseEntity<Set<Long>> getLikedQuizzes(@PathVariable Long userId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        return optionalUser.map(user -> ResponseEntity.ok(user.getLikedQuizId()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{userId}/likes/{quizId}")
    public ResponseEntity<?> unlikeQuiz(@PathVariable Long userId, @PathVariable Long quizId) {
        Optional<User> optionalUser = userRepo.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = optionalUser.get();
        user.getLikedQuizId().remove(quizId);
        userRepo.save(user);
        return ResponseEntity.ok("Quiz unliked");
    }

// FOR RSETTING PASSWORD
@PostMapping("/forgot-password")
public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> payload) {
    String email = payload.get("email");
    Optional<User> userOptional = userRepo.findByEmail(email);

    if (userOptional.isPresent()) {
        User user = userOptional.get();
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setPasswordtokenExpiry(LocalDateTime.now().plusMinutes(15));
        userRepo.save(user);

        String resetLink = "http://localhost:3000/reset-password/" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
        return ResponseEntity.ok("Reset link sent.");
    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
}

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestBody Map<String, String> payload) {
        Optional<User> userOptional = userRepo.findByResetPasswordToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPasswordtokenExpiry().isAfter(LocalDateTime.now())) {
                user.setPassword(passwordEncoder.encode(payload.get("newPassword")));
                user.setResetPasswordToken(null);
                user.setPasswordtokenExpiry(null);
                userRepo.save(user);
                return ResponseEntity.ok("Password updated successfully.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
    }

}
