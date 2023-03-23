package com.demo.springjwt.listener;

import com.demo.springjwt.service.LogInAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
public class AuthenticationFailureListener {

    private LogInAttemptService logInAttemptService;

    @Autowired
    public AuthenticationFailureListener(LogInAttemptService logInAttemptService){
        this.logInAttemptService = logInAttemptService;
    }

    @EventListener
    public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) throws ExecutionException {
       Object principal = event.getAuthentication().getPrincipal();
       if(principal instanceof String){
           String username = (String) event.getAuthentication().getPrincipal();
           logInAttemptService.addUserToLoginAttemptCache(username);
       }
    }
}
