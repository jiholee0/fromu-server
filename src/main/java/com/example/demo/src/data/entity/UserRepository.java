package com.example.demo.src.data.entity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @NotNull List<User> findAll();
    User findById(int userId);
}
