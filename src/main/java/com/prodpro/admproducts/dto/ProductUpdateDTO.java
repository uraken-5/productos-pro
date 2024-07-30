package com.prodpro.admproducts.dto;

import lombok.Data;

@Data
public class ProductUpdateDTO {
	private Long id;
    private String name;
    private String category;
    private Double unitPrice;
    private Integer stock;
}
