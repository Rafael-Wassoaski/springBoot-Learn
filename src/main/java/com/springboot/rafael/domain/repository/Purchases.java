package com.springboot.rafael.domain.repository;

import com.springboot.rafael.domain.entity.Client;
import com.springboot.rafael.domain.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Purchases extends JpaRepository<Purchase, Integer> {
    List<Purchase> findByClient(Client client);
    
    @Query("select p from Purchase p left join fetch p.item where p.id = :id")
    Optional<Purchase> findByIdFetchItems(@Param("id") Integer id);
}
