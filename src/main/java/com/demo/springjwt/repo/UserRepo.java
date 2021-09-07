package com.demo.springjwt.repo;

import com.demo.springjwt.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    User findUserByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = TRUE WHERE u.email = ?1 ")
    int enableUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.failedAttempt = ?1 WHERE u.email = ?2 ")
    void updateFailedAttempts(int failAttempt, String email);
}
