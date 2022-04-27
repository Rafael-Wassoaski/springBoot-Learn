package com.springboot.rafael.service;

import java.util.List;
import java.util.Optional;

import com.springboot.rafael.domain.entity.Purchase;
import com.springboot.rafael.domain.enums.StatusPedido;
import com.springboot.rafael.dto.PurchaseDTO;
import com.springboot.rafael.dto.UpdatePurchaseStatusDTO;

public interface PurchasesService {

	Purchase save(PurchaseDTO purchase);

	List<Purchase> getAll();

	Optional<Purchase> getById(Integer id);
	
	void cancel(StatusPedido statusPedido, Integer id);
}
