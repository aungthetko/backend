package com.demo.springjwt.service;

import com.demo.springjwt.email.EmailService;
import com.demo.springjwt.enumeration.Role;
import com.demo.springjwt.exception.EmailNotFoundException;
import com.demo.springjwt.exception.UserNotFoundException;
import com.demo.springjwt.kafka.UserProducer;
import com.demo.springjwt.modal.User;
import com.demo.springjwt.modal.UserPrincipal;
import com.demo.springjwt.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.demo.springjwt.enumeration.Role.*;

@Service
@Transactional
@Qualifier("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final LogInAttemptService logInAttemptService;
    private final UserProducer userProducer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByUsername(username)
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(username + " can not found in database"));
        validateLoginAttempt(user);
        userRepo.save(user);
        return new UserPrincipal(user);
    }

    private void validateLoginAttempt(User user) {
        if(user.getLocked()){
            if(logInAttemptService.hasExceededMaxAttempts(user.getUsername())){
                user.setLocked(false);
            }else{
                user.setLocked(true);
            }
        }else{
            logInAttemptService.getUserFromLoginAttemptCache(user.getUsername());
        }
    }

    @Override
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User register(String firstName,String password, String lastName, String username, String email, String jobTile, String address) {
        User user = new User();
        if (password.equals("") || password == null){
            password = generatePassword();
        }
        String encodedPassword = encodedPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setAuthorities(ROLE_ADMIN.getAuthorities());
        user.setRole(ROLE_ADMIN.name());
        user.setEnabled(Boolean.TRUE);
        user.setLocked(Boolean.FALSE);
        user.setJobTitle(jobTile);
        user.setAddress(address);
        userProducer.sendMessage(user);
        // userRepo.save(user);
        // To-do
        // emailService.sendNewPasswordToEmail(firstName, email, password);
        LOGGER.info(password);
        return user;
    }

    @Override
    public User findByUsername(String username) throws UserNotFoundException{
        return userRepo.findUserByUsername(username).stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(username + " is not found"));
    }

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        if(email.trim().length() == 0 || email.equals("") || email == null){
            throw new IllegalStateException("Email can not be empty");
        }
        User user = userRepo.findUserByEmail(email).stream().findFirst()
                .orElseThrow(() ->
                        new UserNotFoundException("User was not found"));
        return user;
    }

    @Override
    public User updateUser(String currentUsername,
                           String newFirstName,
                           String newLastName,
                           String newUsername,
                           String newEmail, String role) {
        User currentUser = new User();
        currentUser.setFirstName(newFirstName);
        currentUser.setLastName(newLastName);
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setRole(getRoleEnumName(role).name());
        currentUser.setAuthorities(getRoleEnumName(role).getAuthorities());
        userRepo.save(currentUser);
        return currentUser;
    }

    @Override
    public void resetPassword(String email) throws EmailNotFoundException {
        User user = findByEmail(email);
        if(user != null){
            String password = RandomStringUtils.randomAlphabetic(8);
            user.setPassword(encodedPassword(password));
            userRepo.save(user);
            LOGGER.info("New generated password : " + password);
        }
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepo.findUserByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));
        userRepo.deleteById(user.getId());
    }

    @Override
    public void increaseFailedAttempts(User user) {
        int newFailedAttempt = user.getFailedAttempt() + 1;
        userRepo.updateFailedAttempts(newFailedAttempt, user.getEmail());
    }

    private Role getRoleEnumName(String role){
        return Role.valueOf(role.toUpperCase());
    }

    private String generatePassword(){
        return RandomStringUtils.randomAlphabetic(8);
    }

    public String encodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
