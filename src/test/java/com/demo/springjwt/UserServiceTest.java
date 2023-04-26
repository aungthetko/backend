package com.demo.springjwt;

import com.demo.springjwt.exception.EmailNotFoundException;
import com.demo.springjwt.modal.User;
import com.demo.springjwt.repo.UserRepo;
import com.demo.springjwt.service.LogInAttemptService;
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
    @Mock
    LogInAttemptService logInAttemptService;

    String firstName;
    String lastName;
    String email;
    String username;
    String jobTitle;
    String address;

    @BeforeEach
    void setUp(){
        System.out.println("Execution @BeforeEach method.");
        firstName = "Aung";
        lastName = "Ko";
        email = "aung@hotmail.com";
        username = "aung";
        jobTitle = "Java Developer";
        address = "Iceland";
    }

    @Test
    @DisplayName("Create User Testing")
    void testCreateUser_whenUserDetailsProvided_returnUserObject(){
        User createdUser =
                userService.register(firstName, lastName, username, email, jobTitle, address);
        assertNotNull(createdUser, "Register user can not be return null");
    }

    @Test
    @DisplayName("User's Email Testing")
    void testFindUser_ifEmailIsEmpty_shouldThrownIllegalStateException(){
        String email = "";
        String exceptedException = "Email can not be empty";
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            userService.findByEmail(email);
        }, "Email should not be empty");
        assertEquals(exceptedException, thrown.getMessage(), "Exception Message");
    }
}
