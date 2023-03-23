package com.demo.springjwt.service;

import com.demo.springjwt.modal.User;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LogInAttemptService {

    public static final int MAXIMUM_NUMBER_OF_ATTEMPT = 5;
    public static final int ATTEMPT_INCREMENT = 1;
    private LoadingCache<String, Integer> logInAttemptCache;

    public LogInAttemptService(){
        super();
        logInAttemptCache = CacheBuilder.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .maximumSize(100).build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String key) throws Exception {
                        return 0;
                    }
                });
    }

    public void getUserFromLoginAttemptCache(String username){
        logInAttemptCache.invalidate(username);
    }

    public void addUserToLoginAttemptCache(String username){
        int attempts = 0;
        try {
            attempts = ATTEMPT_INCREMENT + logInAttemptCache.get(username);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        logInAttemptCache.put(username, attempts);
    }

    public boolean hasExceededMaxAttempts(String username){
        try {
            return logInAttemptCache.get(username) >= MAXIMUM_NUMBER_OF_ATTEMPT;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
