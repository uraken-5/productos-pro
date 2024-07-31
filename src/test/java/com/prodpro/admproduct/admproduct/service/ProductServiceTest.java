package com.prodpro.admproduct.admproduct.service;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import com.prodpro.admproducts.ProductosProApplication;
import com.prodpro.admproducts.dto.ProductCreateDTO;
import com.prodpro.admproducts.dto.ProductUpdateDTO;
import com.prodpro.admproducts.mapper.ProductMapper;
import com.prodpro.admproducts.model.Product;
import com.prodpro.admproducts.repository.ProductRepository;
import com.prodpro.admproducts.service.ProductService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ProductosProApplication.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;
    
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ProductService productService;
    
    private static final String ESTADISTICAS_URL = "http://localhost:8082/api/estadisticas/registrar?categoria=";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProduct() {
        ProductCreateDTO createDTO = new ProductCreateDTO();
        createDTO.setName("Test Product");
        createDTO.setCategory("Test Category");
        createDTO.setUnitPrice(10.0);
        createDTO.setStock(100);

        Product product = new Product();
        product.setName(createDTO.getName());
        product.setCategory(createDTO.getCategory());
        product.setUnitPrice(createDTO.getUnitPrice());
        product.setStock(createDTO.getStock());

        when(productMapper.toProduct(any(ProductCreateDTO.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        
     // Simular la llamada a RestTemplate
        when(restTemplate.postForObject(eq(ESTADISTICAS_URL + createDTO.getCategory()), isNull(), eq(Void.class))).thenReturn(null);


        Product createdProduct = productService.createProduct(createDTO);

        assertNotNull(createdProduct);
        assertEquals(createDTO.getName(), createdProduct.getName());

        verify(productMapper, times(1)).toProduct(any(ProductCreateDTO.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct() {
        Long productId = 1L;
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setName("Update Product");
        updateDTO.setCategory("Update Category");
        updateDTO.setUnitPrice(20.0);
        updateDTO.setStock(200);

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Update Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        Product updatedProduct = productService.updateProduct(productId, updateDTO);

        assertNotNull(updatedProduct);
        assertEquals(updateDTO.getName(), updatedProduct.getName());

        verify(productRepository, times(1)).findById(productId);
        verify(productMapper, times(1)).updateProductFromDto(any(ProductUpdateDTO.class), any(Product.class));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        Long productId = 1L;

        when(productRepository.existsById(productId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(productId);

        productService.deleteProduct(productId);

        verify(productRepository, times(1)).existsById(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    public void testListProducts() {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.listProducts();

        assertNotNull(products);
        assertEquals(2, products.size());

        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testGetProductForId() {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.getProductForId(productId);

        assertTrue(foundProduct.isPresent());
        assertEquals(productId, foundProduct.get().getId());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    public void testSearchProductsByName() {
        String productName = "Test Product";
        Product product = new Product();
        product.setId(1L);
        product.setName(productName);

        when(productRepository.findByNameContaining(productName)).thenReturn(Arrays.asList(product));

        List<Product> products = productService.searchProductsByName(productName);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(productName, products.get(0).getName());

        verify(productRepository, times(1)).findByNameContaining(productName);
    }
}
