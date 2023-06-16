package com.demo.springjwt.entity;

import com.demo.springjwt.enumeration.Role;
import com.demo.springjwt.exception.EmailNotFoundException;
import com.demo.springjwt.modal.User;
import com.demo.springjwt.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

@DataJpaTest
public class UserEntityIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;
    @MockBean
    private UserService userService;

    @Test
    @Disabled
    void first_test() throws EmailNotFoundException {
        User user = new User();
        user.setId(10L);
        user.setFirstName("Astro");
        user.setLastName("Smith");
        user.setUsername("astro");
        user.setPassword("123");
        user.setEmail("astro@hotmail.com");
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setLocked(false);
        user.setJobTitle("Java");
        user.setAddress("GreenLand");
        user.setEnabled(true);
        user.setFailedAttempt(0);
        user.setLockTime(new Date());

        //Arrange
        testEntityManager.persist(user);
        testEntityManager.flush();

    }
}
