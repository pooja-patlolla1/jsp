
package com.example.loginapp.repository;

import com.example.loginapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String userPassword);

    User findEmail(String email);
}

