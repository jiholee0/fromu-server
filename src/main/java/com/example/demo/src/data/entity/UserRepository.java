package com.example.demo.src.data.entity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @NotNull List<User> findAll();
    Optional<User> findById(int userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserCode(String userCode);
}
