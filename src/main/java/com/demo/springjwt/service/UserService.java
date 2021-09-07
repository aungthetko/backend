package com.demo.springjwt.service;

import com.demo.springjwt.modal.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<User> findAllUsers();

    User register(String firstName, String lastName, String username, String email);

    User findByUsername(String username);

    User findByEmail(String email);

    void resetPassword(String email);

    void deleteUser(String username);

    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role);

    void increaseFailedAttempts(User user);
}
