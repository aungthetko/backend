package com.demo.springjwt.resource;

import com.demo.springjwt.modal.User;
import com.demo.springjwt.repo.UserRepo;
import com.demo.springjwt.service.UserService;
import com.demo.springjwt.utility.JWTTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebMvcTest(controllers = UserResource.class,
excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class UserResourceWebLayerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UserService userService;
    @MockBean
    UserDetailsService userDetailsService;
    @MockBean
    JWTTokenProvider jwtTokenProvider;
    @MockBean
    UserRepo userRepo;

    @Test
    @DisplayName("Testing create user")
    void testCreateUser_whenValidUserProvided_shouldReturnUserDetails()
            throws Exception {

        User user = new User();
        user.setFirstName("Alice");
        user.setLastName("Wonderland");
        user.setUsername("alice");
        user.setPassword("password");
        user.setEmail("alice@hotmail.com");
        user.setJobTitle("Spring/Angular");
        user.setAddress("NYC");

        Mockito.when(userService.register(user.getFirstName(), user.getLastName(), user.getUsername(),
                user.getEmail(), user.getJobTitle(), user.getAddress()))
                .thenReturn(user);

        // Arrange
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user));

        //Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        System.out.println("Response object : " + response);
        User createdUser = new ObjectMapper().readValue(response, User.class);

        //Assert
        assertNotNull(response, "MVCResult response should not be empty.");
        assertEquals(user.getFirstName(), createdUser.getFirstName(),
                "User's first name can not be blank");

    }
}
