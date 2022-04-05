package com.springboot.rafael.domain.repository;

import com.springboot.rafael.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Products extends JpaRepository<Products, Integer> {
    List<Product> findByDescriptionLike(String description);

}
