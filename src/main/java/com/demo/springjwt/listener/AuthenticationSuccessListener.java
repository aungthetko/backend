package com.demo.springjwt.listener;

import com.demo.springjwt.modal.User;
import com.demo.springjwt.modal.UserPrincipal;
import com.demo.springjwt.service.LogInAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener {

    private LogInAttemptService logInAttemptService;

    @Autowired
    public AuthenticationSuccessListener(LogInAttemptService logInAttemptService){
        this.logInAttemptService = logInAttemptService;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event){
        Object principal = event.getAuthentication().getPrincipal();
        if(principal instanceof UserPrincipal){
            UserPrincipal newUser = (UserPrincipal) event.getAuthentication().getPrincipal();
            logInAttemptService.getUserFromLoginAttemptCache(newUser.getUsername());

        }
    }
}
