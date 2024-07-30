package com.prodpro.admproducts.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.prodpro.admproducts.dto.ProductCreateDTO;
import com.prodpro.admproducts.dto.ProductUpdateDTO;
import com.prodpro.admproducts.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toProduct(ProductCreateDTO productCreateDTO);

    void updateProductFromDto(ProductUpdateDTO productUpdateDTO, @MappingTarget Product product);
}