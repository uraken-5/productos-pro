package com.prodpro.admproducts.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prodpro.admproducts.dto.ProductCreateDTO;
import com.prodpro.admproducts.dto.ProductUpdateDTO;
import com.prodpro.admproducts.exceptionsmanager.exceptions.ProductNotFoundException;
import com.prodpro.admproducts.model.Product;
import com.prodpro.admproducts.service.ProductService;



@RestController
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
		logger.debug("Request to create product");
		Product nuevoProducto = productService.createProduct(productCreateDTO);
		logger.info("Product created with ID {}", nuevoProducto.getId());
        return ResponseEntity.ok(nuevoProducto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO productUpdateDTO) {
    	logger.debug("Request to update product with ID {}", id);
    	Product productoActualizado = productService.updateProduct(id, productUpdateDTO);
    	logger.info("Product with ID {} updated", productoActualizado.getId());
    	return productoActualizado != null ? ResponseEntity.ok(productoActualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    	logger.debug("Request to delete product with ID {}", id);
        productService.deleteProduct(id);
        logger.info("Product with ID {} deleted", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> listProduct() {
        return ResponseEntity.ok(productService.listProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
    	logger.debug("Request to get product with ID {}", id);
        return productService.getProductForId(id)
                .map(ResponseEntity::ok)
                .orElseThrow(()->new ProductNotFoundException(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> getProductByName(@RequestParam String nombre) {
        return ResponseEntity.ok(productService.searchProductsByName(nombre));
    }
}
