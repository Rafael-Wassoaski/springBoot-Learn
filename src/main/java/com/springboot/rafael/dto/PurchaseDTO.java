package com.springboot.rafael.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//{
//	"client":1,
//	"total": 100,
//	"itemPurchases": [{productId: 1, amount: 10}, {productId:2 , amount: 5}]
//}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDTO {
	private Integer idClient;
	private BigDecimal total;
	private List<ItensPurchaseDTO> itemPurchases;

}
