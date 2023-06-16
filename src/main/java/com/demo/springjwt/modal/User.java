package com.demo.springjwt.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Size(min = 2)
    @Column(nullable = false, length = 50)
    private String firstName;
    @Column(nullable = false, length = 50)
    private String lastName;
    @Column(nullable = false, length = 50)
    private String username;
    @Column(nullable = false)
    private String password;
    @Email
    @Column(nullable = false,unique = true, length = 120)
    private String email;
    private String role;
    private String[] authorities;
    public Boolean locked;
    private String jobTitle;
    private String address;
    private Boolean enabled;
    private int failedAttempt;
    private Date lockTime;

}
