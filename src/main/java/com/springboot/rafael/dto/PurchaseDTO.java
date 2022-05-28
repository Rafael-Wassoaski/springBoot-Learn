package com.springboot.rafael.dto;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotNull;
import com.springboot.rafael.validation.NotEmptyList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// {
// "client":1,
// "total": 100,
// "itemPurchases": [{productId: 1, amount: 10}, {productId:2 , amount: 5}]
// }

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDTO {
	@NotNull(message = "O código do cliente não pode ser vazio")
	private Integer idClient;

	@NotNull(message = "O total do cliente não pode ser vazio")
	private BigDecimal total;

	@NotEmptyList(message = "Não é possível realizar um pedido sem elementos")
	private List<ItensPurchaseDTO> itemPurchases;

}
