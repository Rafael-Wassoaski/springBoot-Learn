package com.springboot.rafael.domain.repository;

import com.springboot.rafael.domain.entity.ItemPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItensPurchase extends JpaRepository<ItemPurchase, Integer> {
}
