package com.springboot.rafael.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfosItemsDTO {
	private String description;
	private BigDecimal price;
	private Integer amount;

}
