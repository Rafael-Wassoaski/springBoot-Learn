package com.springboot.rafael.api.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.springboot.rafael.domain.entity.ItemPurchase;
import com.springboot.rafael.domain.entity.Product;
import com.springboot.rafael.domain.entity.Purchase;
import com.springboot.rafael.domain.enums.StatusPedido;
import com.springboot.rafael.dto.InfosItemsDTO;
import com.springboot.rafael.dto.InfosPurchaseDTO;
import com.springboot.rafael.dto.PurchaseDTO;
import com.springboot.rafael.dto.UpdatePurchaseStatusDTO;
import com.springboot.rafael.exception.RuleException;
import com.springboot.rafael.service.PurchasesService;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

	private PurchasesService purchasesService;

	public PurchaseController(PurchasesService service) {
		this.purchasesService = service;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Integer save(@RequestBody PurchaseDTO dto) {
		Purchase purchase = purchasesService.save(dto);

		return purchase.getId();
	}

	@GetMapping
	public List<Purchase> getPurchases() {
		return purchasesService.getAll();
	}

	@GetMapping("/{id}")
	public InfosPurchaseDTO getPurchasesById(@PathVariable Integer id) {
		return purchasesService.getById(id).map(pruchase -> this.convertToPurchaseDTO(pruchase))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
	}
	
	@PatchMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void cancelPurchase(@RequestBody UpdatePurchaseStatusDTO purchaseDTO, @PathVariable Integer id){
		StatusPedido newStatus = StatusPedido.valueOf(purchaseDTO.getNewStatus());
		purchasesService.cancel(newStatus, id);
	}

	private InfosPurchaseDTO convertToPurchaseDTO(Purchase purchase) {
		return InfosPurchaseDTO
				.builder()
				.code(purchase.getId())
				.nameClient(purchase.getClient().getName())
				.status(purchase.getStatus().name())
				.total(purchase.getTotal()).items(InfosItemsDTO(purchase.getItemPurchases())).build();

	}

	private List<InfosItemsDTO> InfosItemsDTO(List<ItemPurchase> itemPurchases) {
		if(CollectionUtils.isEmpty(itemPurchases)) {
			return Collections.EMPTY_LIST;
		}
		
		Stream <ItemPurchase> itensStream =  itemPurchases.stream();
				
		List<InfosItemsDTO> dtos = itensStream.map(item -> 
		InfosItemsDTO
				.builder()
				.description(item.getProductId().getDescription())
				.price(item.getProductId().getPrice())
				.amount(item.getAmount())
				.build()
				).collect(Collectors.toList());
		
		return dtos;
	}

}
