package com.springboot.rafael.service;

import java.util.List;
import java.util.Optional;

import com.springboot.rafael.domain.entity.Purchase;
import com.springboot.rafael.dto.PurchaseDTO;

public interface PurchasesService {

	Purchase save(PurchaseDTO purchase);

	List<Purchase> getAll();

	Optional<Purchase> getById(Integer id);
}
