package com.demo.springjwt;

import com.demo.springjwt.exception.UserNotFoundException;
import com.demo.springjwt.modal.User;
import com.demo.springjwt.repo.UserRepo;
import com.demo.springjwt.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UserRepo userRepo;
    @Mock
    PasswordEncoder passwordEncoder;

    String firstName;
    String lastName;
    String email;
    String username;
    String jobTitle;
    String address;
    String password;

    @BeforeEach
    void setUp(){
        System.out.println("Execution @BeforeEach method.");
        firstName = "Aung";
        lastName = "Ko";
        password = "";
        email = "aung@hotmail.com";
        username = "aung";
        jobTitle = "Java Developer";
        address = "Iceland";
    }

    @Test
    @DisplayName("Create User Testing")
    void testCreateUser_whenUserDetailsProvided_returnUserObject(){
        User createdUser =
                userService.register(firstName,password, lastName, username, email, jobTitle, address);
        assertNotNull(createdUser, "Register user can not be return null");
    }

    @Test
    @DisplayName("Testing Find User By Email")
    void testFindUserByEmail_ifEmailIsEmpty_shouldThrownIllegalStateException(){
        String email = "";
        String exceptedException = "Email can not be empty";
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            userService.findByEmail(email);
        }, "Email should not be empty");
        assertEquals(exceptedException, thrown.getMessage(), "Exception Message");

    }

    @Test
    @DisplayName("Testing Find User By Username")
    void testFindUserByUsername_ifUserNotFound_shouldThrown(){
        String username = "";
        assertThrows(UserNotFoundException.class, () -> {
            userService.findByUsername(username);
        }, "User was not found");
        Mockito.verify(userRepo, Mockito.times(1))
                .findUserByUsername(username);
    }
}
