package com.demo.springjwt.controller;

import com.demo.springjwt.modal.User;

import java.util.List;
import java.util.Optional;

public interface UserApiService {

    List<User> findAllUsers();

    User findUserById(Long id);

    User saveUser(String firstName, String lastName, String username, String password,String jobTilte, String email);

    User updateUser(Long id, User user);

    void deleteUserById(Long id);

    public String confirmToken(String token);

    public int enableAppUser(String email);
}
