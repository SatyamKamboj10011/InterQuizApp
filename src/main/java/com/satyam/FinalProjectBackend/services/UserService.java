package com.satyam.FinalProjectBackend.services;

import com.satyam.FinalProjectBackend.db.UserRepo;
import com.satyam.FinalProjectBackend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    public User registeredUser(User user){
        User newUser = userRepo.save(user);

        // Send Welcome Email
        String subject = "Welcome to Quiz App!";
        String body = "Hi " + user.getUsername() + ",\n\n"
                + "Thank you for registering at Quiz App.\n"
                + "You can now log in and start participating in exciting quizzes!\n\n"
                + "Best regards,\nQuiz App Team";
        emailService.sendEmail(user.getEmail(), subject, body);

        return newUser;
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
    public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setRole(updatedUser.getRole());

            User savedUser = userRepo.save(existingUser);

            // Send Email Notification when role is updated
            String subject = "Your Role Has Been Updated!";
            String body = "Hello " + updatedUser.getUsername() + ",\n\n"
                    + "Your role has been updated to " + updatedUser.getRole() + ".\n"
                    + "You can now log in and access your updated privileges.\n\n"
                    + "Regards,\nQuiz App Team";
            emailService.sendEmail(updatedUser.getEmail(), subject, body);

            return savedUser;
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
    public Optional<User> getUserByUsername(String username){
        return userRepo.findByUsername(username);
    }
    public Optional<User> authenticate(String email, String password) {
        return userRepo.findByEmailAndPassword(email, password);
    }
    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isPresent()) {
            User userToDelete = optionalUser.get();

            // Send Email Notification before deleting
            String subject = "Account Deleted!";
            String body = "Hello " + userToDelete.getUsername() + ",\n\n"
                    + "Your account has been successfully deleted.\n"
                    + "If you think this was a mistake, please contact support.\n\n"
                    + "Regards,\nQuiz App Team";
            emailService.sendEmail(userToDelete.getEmail(), subject, body);

            // Delete User
            userRepo.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }

    }

}
