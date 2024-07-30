package com.prodpro.admproducts.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.prodpro.admproducts.dto.ProductCreateDTO;
import com.prodpro.admproducts.dto.ProductUpdateDTO;
import com.prodpro.admproducts.exceptionsmanager.exceptions.ProductNotFoundException;
import com.prodpro.admproducts.mapper.ProductMapper;
import com.prodpro.admproducts.model.Product;
import com.prodpro.admproducts.repository.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductMapper productMapper;
    
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public Product createProduct(ProductCreateDTO productCreateDTO) {
    	Product product = productMapper.toProduct(productCreateDTO);
    	logger.info("Product created with ID: {}", product.getId());
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductUpdateDTO productUpdateDTO) {
    	Optional<Product> existingProductOptional = productRepository.findById(id);
    	if(!existingProductOptional.isPresent()) {
    		logger.warn("Product with ID {} not found for update", id);
    		throw new ProductNotFoundException(id);
    	}
    	
    	Product existingProduct = existingProductOptional.get();
    	productMapper.updateProductFromDto(productUpdateDTO, existingProduct);
    	logger.info("Product with ID {} updated", existingProduct.getId());
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
    	  if (!productRepository.existsById(id)) {
    		  logger.warn("Product with ID {} not found for deletion", id);
              throw new ProductNotFoundException(id);
          }
          productRepository.deleteById(id);
    }

    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductForId(Long id) {
    	logger.debug("Fetching product with ID {}", id);
        return productRepository.findById(id);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContaining(name);
    }
}
