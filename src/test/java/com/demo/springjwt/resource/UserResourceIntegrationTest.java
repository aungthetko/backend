package com.demo.springjwt.resource;

import com.demo.springjwt.modal.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource("classpath:application.yml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    private JSONObject json;

    @BeforeEach
    void setup() throws JSONException {
        json = new JSONObject();
        json.put("firstName", "Alison");
        json.put("lastName", "Sona");
        json.put("username", "alison");
        json.put("password", "123456789");
        json.put("email", "alison@hotmail.com");
        json.put("jobTitle", "UI/UX");
        json.put("address", "Ice Land");
    }

    @Test
    @DisplayName("Test create user with JSON")
    @Order(1)
    void testCreateUser_whenValidDetailsProvided_returnUser() throws JSONException {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(json.toString(), headers);
        //Act
        ResponseEntity<User> createdUser =
                testRestTemplate.postForEntity("/user/register",
                        request, User.class);
        User responseUser = createdUser.getBody();

        //Assert
        assertEquals(HttpStatus.CREATED, createdUser.getStatusCode());
        assertEquals(json.getString("firstName"), responseUser.getFirstName(),
                "User's first name is incorrect.");
    }

    @Test
    @DisplayName("Get message / require JWT")
    @Order(2)
    void testGetUsers_WhenMissingJWT_return403(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept", "application/json");

        HttpEntity httpEntity = new HttpEntity(null, httpHeaders);

        ResponseEntity<String> response = testRestTemplate.exchange("/user/message",
                HttpMethod.GET,
                httpEntity,
                String.class);
        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(),
                "HttpStatusCode 403 should has been return");
    }

    @Test
    @DisplayName("/login user - return JWT Token")
    @Order(3)
    void testLoginUser_whenValidReturnJWTTokenHeader() throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        JSONObject loginUser = new JSONObject();
        loginUser.put("username", "alison");
        loginUser.put("password", "123456789");

        HttpEntity request = new HttpEntity(loginUser.toString(), headers);

        ResponseEntity<User> response = testRestTemplate.postForEntity("/user/login",
                request, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "HttpStatus code should be 200");
        assertNotNull(response.getHeaders().getValuesAsList("Jwt-Token")
                .get(0), "Response should be contain Jwt-Token with JWT token");
    }
}
