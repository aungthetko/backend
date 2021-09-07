package com.demo.springjwt.controller;

import com.demo.springjwt.modal.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserApiService userApiService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok().body(userApiService.findAllUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<User> addNewUser(@RequestBody User user){
        User newUser = userApiService.saveUser(user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getEmail());
        return ResponseEntity.ok().body(newUser);
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam String token){
        return userApiService.confirmToken(token);
    }
}
