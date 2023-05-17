package com.demo.springjwt.resource;

import com.demo.springjwt.modal.User;
import com.demo.springjwt.service.UserService;
import com.demo.springjwt.utility.JWTTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
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

    User user;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setFirstName("Alice");
        user.setLastName("Wonderland");
        user.setUsername("alice");
        user.setPassword("password");
        user.setEmail("alice@hotmail.com");
        user.setJobTitle("Spring/Angular");
        user.setAddress("NYC");
    }

    @Test
    @DisplayName("Testing create user")
    void testCreateUser_whenValidUserProvided_shouldReturnUserDetails()
            throws Exception {

        Mockito.when(userService.register(user.getFirstName(),user.getPassword(), user.getLastName(), user.getUsername(),
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
        User createdUser = new ObjectMapper().readValue(response, User.class);

        //Assert
        assertNotNull(response, "MVCResult response should not be empty.");
        assertEquals(user.getFirstName(), createdUser.getFirstName(),
                "User's first name can not be blank");
    }

    @Test
    @DisplayName("Testing firstName is empty - return 400")
    void testCreateUser_whenFirstNameIsNotProvided_shouldReturn400BadRequest() throws Exception {
        // Arrange
        user.setFirstName("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        //Assert
        assertEquals(HttpStatus.BAD_REQUEST.value(),
                mvcResult.getResponse().getStatus(),
                "Incorrect HTTP Status code return.");
    }

    @Test
    @DisplayName("Testing first name shorter than 2 - return 400")
    void testCreateUser_whenFirstNameIsShorterThan2_shouldReturn400BadRequest() throws Exception {
        user.setFirstName("a");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(),
                mvcResult.getResponse().getStatus(),
                "Incorrect HTTP Status code return.");
    }
}
