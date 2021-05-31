package com.techboard.springbootmultithreading.executor.api.repository;

import com.techboard.springbootmultithreading.executor.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
