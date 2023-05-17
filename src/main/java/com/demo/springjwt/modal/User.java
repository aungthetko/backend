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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Size(min = 2)
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    @Email
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
