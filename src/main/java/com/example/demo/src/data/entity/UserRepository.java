package com.example.demo.src.data.entity;
import org.hibernate.annotations.Where;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @NotNull List<User> findAll();
    Optional<User> findById(int userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserCode(String userCode);
}
