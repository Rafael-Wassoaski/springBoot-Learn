package com.springboot.rafael.service.implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.springboot.rafael.domain.entity.Client;
import com.springboot.rafael.domain.entity.ItemPurchase;
import com.springboot.rafael.domain.entity.Product;
import com.springboot.rafael.domain.entity.Purchase;
import com.springboot.rafael.domain.repository.Clients;
import com.springboot.rafael.domain.repository.ItensPurchase;
import com.springboot.rafael.domain.repository.Products;
import com.springboot.rafael.domain.repository.Purchases;
import com.springboot.rafael.dto.ItensPurchaseDTO;
import com.springboot.rafael.dto.PurchaseDTO;
import com.springboot.rafael.exception.RuleException;
import com.springboot.rafael.service.PurchasesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImplementation implements PurchasesService {

	private final Purchases purchases;
	private final Clients clients;
	private final Products products;
	private final ItensPurchase itensPurchase;

	@Override
	@Transactional
	public Purchase save(PurchaseDTO purchaseDTO) {
		Integer clientId = purchaseDTO.getIdClient();
		Client client = clients.findById(clientId)
				.orElseThrow(() -> new RuleException("Código de cliente não encontrado"));
		Purchase purchase = new Purchase();

		purchase.setTotal(purchaseDTO.getTotal());
		purchase.setDatePurchase(LocalDate.now());
		purchase.setClient(client);
				
		List<ItemPurchase> purchaseItems = convertPurchaseItens(purchase, purchaseDTO.getItemPurchases());
		System.out.println(purchaseItems.get(0).toString());
		itensPurchase.saveAll(purchaseItems);
		purchases.save(purchase);

		purchase.setItemPurchases(purchaseItems);
		
		return purchase;
	}

	private List<ItemPurchase> convertPurchaseItens(Purchase purchase, List<ItensPurchaseDTO> itens) {
		if (itens.isEmpty()) {
			throw new RuleException("Não é possível criar um pedido sem produtos");
		}

		return itens.stream().map(dto -> {
			Integer productId = dto.getProductId();
			ItemPurchase itemPurchase = new ItemPurchase();
			Product product = products.findById(productId)
					.orElseThrow(() -> new RuleException("Produto não encontrado: " + productId));

			itemPurchase.setAmount(dto.getAmount());
			itemPurchase.setPurchaseId(purchase);
			itemPurchase.setProductId(product);

			return itemPurchase;
		}).collect(Collectors.toList());
	}

	@Override
	public List<Purchase> getAll() {
		// TODO Auto-generated method stub
		return purchases.findAll();
	}

	@Override
	public Optional<Purchase> getById(Integer id) {
		// TODO Auto-generated method stub
		return purchases.findByIdFetchItems(id);
	}

	
}
