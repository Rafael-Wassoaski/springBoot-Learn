package com.springboot.rafael.domain.repository;

import com.springboot.rafael.domain.entity.Client;
import com.springboot.rafael.domain.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Purchases extends JpaRepository<Purchase, Integer> {
    List<Purchase> findByClient(Client client);

}
