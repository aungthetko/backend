package com.demo.springjwt.resource;

import com.demo.springjwt.modal.HttpResponse;
import com.demo.springjwt.modal.User;
import com.demo.springjwt.modal.UserPrincipal;
import com.demo.springjwt.service.UserService;
import com.demo.springjwt.utility.JWTTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserResource {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTTokenProvider jwtTokenProvider;

    @GetMapping
    public String sayHello(){
        return "Hello World";
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok().body(userService.findAllUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user){
        lockAccount(user);
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJWTHeader(userPrincipal);
        return ResponseEntity.ok().headers(jwtHeader).body(loginUser);
    }

    @GetMapping("/resetpassword/{email}")
    public ResponseEntity<HttpResponse> resetPassword(@PathVariable("email") String email){
        userService.resetPassword(email);
        return response(HttpStatus.OK, "Password successfully changed");
    }

    @PostMapping("/update")
    public ResponseEntity<User> updateUser(@RequestParam("currentUsername") String currentUsername,
                                           @RequestParam("firstName") String firstName,
                                           @RequestParam("lastName") String lastName,
                                           @RequestParam("username") String username,
                                           @RequestParam("email") String email,
                                           @RequestParam(value = "role", required = false) String role){
        User newUser = userService.updateUser(currentUsername, firstName, lastName, username, email, role);
        return ResponseEntity.ok().body(newUser);
    }

    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username){
        userService.deleteUser(username);
        return response(HttpStatus.OK, "User successfully deleted");
    }

    private HttpHeaders getJWTHeader(UserPrincipal userPrincipal) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Jwt-Token", jwtTokenProvider.generateToken(userPrincipal));
        return httpHeaders;
    }

    private void lockAccount(User user) {
        User getUser = userService.findByUsername(user.getUsername());
        if (user.getUsername() != getUser.getUsername() &&
                user.getPassword() != getUser.getPassword()){
            userService.increaseFailedAttempts(getUser);
            if (getUser.getFailedAttempt() >= 3 - 1){
                getUser.setLocked(Boolean.FALSE);
                System.out.println("Your account has been locked");
            }
        }
    }

    private void authenticate(String username, String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message){
        return new ResponseEntity<>(
                new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase()), httpStatus);
    }
}
