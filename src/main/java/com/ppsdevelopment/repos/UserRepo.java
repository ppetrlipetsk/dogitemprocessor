package com.ppsdevelopment.repos;


import com.ppsdevelopment.domain.reserv.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
