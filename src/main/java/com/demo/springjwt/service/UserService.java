package com.demo.springjwt.service;

import com.demo.springjwt.exception.EmailNotFoundException;
import com.demo.springjwt.modal.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<User> findAllUsers();

    User register(String firstName,String password, String lastName, String username, String email, String jobTile, String address);

    User findByUsername(String username);

    User findByEmail(String email) throws EmailNotFoundException;

    void resetPassword(String email) throws EmailNotFoundException;

    void deleteUser(String username);

    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role);

    void increaseFailedAttempts(User user);
}
