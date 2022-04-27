package com.springboot.rafael.domain.repository;

import java.util.Optional;
import com.springboot.rafael.domain.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<CustomUser, Integer>{

    public Optional<CustomUser> findByUsername(String username);
    
}
