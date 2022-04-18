package com.springboot.rafael.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfosPurchaseDTO {

	private Integer code;
	private String nameClient;
	private BigDecimal total;
	private List<InfosItemsDTO> items;
}
