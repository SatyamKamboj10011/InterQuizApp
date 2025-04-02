package com.satyam.FinalProjectBackend.services;

import com.satyam.FinalProjectBackend.db.UserRepo;
import com.satyam.FinalProjectBackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    // ========================
    // Register New User
    // ========================
    public User registeredUser(User user) {
        User newUser = userRepo.save(user);

        try {
            // Send Welcome Email (Skip for Dummy Users)
            if (!user.getEmail().endsWith("@example.com")) {
                String subject = "Welcome to Quiz App!";
                String body = "Hi " + user.getUsername() + ",\n\n"
                        + "Thank you for registering at Quiz App.\n"
                        + "You can now log in and start participating in exciting quizzes!\n\n"
                        + "Best regards,\nSatyam Kamboj,\nQuiz App Team";
                emailService.sendEmail(user.getEmail(), subject, body);
            } else {
                System.out.println("Skipping email for dummy user: " + user.getEmail());
            }
        } catch (Exception e) {
            System.out.println("Email Sending Failed: " + e.getMessage());
        }
        return newUser;
    }

    // ========================
    // Get All Users
    // ========================
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // ========================
    // Update User and Send Email if Role is Updated
    // ========================
    public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());

            User savedUser = userRepo.save(existingUser);

            // Send Email Notification when role is updated (Skip for Dummy Users)
            if (!updatedUser.getEmail().endsWith("@example.com")) {
                String subject = "Your Role Has Been Updated!";
                String body = "Hello " + updatedUser.getUsername() + ",\n\n"
                        + "Your role has been updated to " + updatedUser.getRole() + ".\n"
                        + "You can now log in and access your updated privileges.\n\n"
                        + "Regards,\nSatyam Kamboj,\nQuiz App Team";
                emailService.sendEmail(updatedUser.getEmail(), subject, body);
            } else {
                System.out.println("Skipping role update email for dummy user: " + updatedUser.getEmail());
            }

            return savedUser;
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    // ========================
    // Get User by Username
    // ========================
    public Optional<User> getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    // ========================
    // Authenticate User
    // ========================
    public Optional<User> authenticate(String email, String password) {
        return userRepo.findByEmailAndPassword(email, password);
    }

    // ========================
    // Delete User and Send Email Before Deletion
    // ========================
    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User userToDelete = optionalUser.get();

            // Check if email is not null before sending email
            if (userToDelete.getEmail() != null && !userToDelete.getEmail().isEmpty()) {
                // Send Email Notification before deleting
                String subject = "Account Deleted!";
                String body = "Hello " + userToDelete.getUsername() + ",\n\n"
                        + "Your account has been successfully deleted.\n"
                        + "If you think this was a mistake, please contact support.\n\n"
                        + "Regards,\nSatyam Kamboj,\nQuiz App Team";

                if (!userToDelete.getEmail().endsWith("@example.com")) {
                    emailService.sendEmail(userToDelete.getEmail(), subject, body);
                } else {
                    System.out.println("Skipping email for dummy user: " + userToDelete.getEmail());
                }
            } else {
                System.out.println("User email is null, skipping email notification.");
            }

            // Delete User
            userRepo.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public ResponseEntity<String> likeQuiz(Long userId, Long quizId) {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.getLikedQuizId().add(quizId);
            userRepo.save(user);
            return ResponseEntity.ok("Quiz liked!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    public ResponseEntity<String> unlikeQuiz(Long userId, Long quizId) {
        Optional<User> userOpt = userRepo.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.getLikedQuizId().remove(quizId);
            userRepo.save(user);
            return ResponseEntity.ok("Quiz unliked!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    public ResponseEntity<Set<Long>> getLikedQuizIds(Long userId) {
        Optional<User> userOpt = userRepo.findById(userId);
        return userOpt.map(user -> ResponseEntity.ok(user.getLikedQuizId()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


}

