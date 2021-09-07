package com.demo.springjwt.email;

public interface EmailSender {
    void send(String to, String email);
}
