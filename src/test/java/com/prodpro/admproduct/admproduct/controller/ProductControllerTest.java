package com.prodpro.admproduct.admproduct.controller;




import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prodpro.admproducts.ProductosProApplication;
import com.prodpro.admproducts.controller.ProductController;
import com.prodpro.admproducts.dto.ProductCreateDTO;
import com.prodpro.admproducts.dto.ProductUpdateDTO;
import com.prodpro.admproducts.model.Product;
import com.prodpro.admproducts.service.ProductService;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ProductosProApplication.class)
public class ProductControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testCreateProduct() throws Exception {
        ProductCreateDTO createDTO = new ProductCreateDTO();
        createDTO.setName("Test Product");
        createDTO.setCategory("Test Category");
        createDTO.setUnitPrice(10.0);
        createDTO.setStock(100);

        Product product = new Product();
        product.setId(1L);
        product.setName(createDTO.getName());
        product.setCategory(createDTO.getCategory());
        product.setUnitPrice(createDTO.getUnitPrice());
        product.setStock(createDTO.getStock());

        when(productService.createProduct(any(ProductCreateDTO.class))).thenReturn(product);

        mockMvc.perform(post("/api/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()));

        verify(productService, times(1)).createProduct(any(ProductCreateDTO.class));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Long productId = 1L;
        ProductUpdateDTO updateDTO = new ProductUpdateDTO();
        updateDTO.setName("Test Product");
        updateDTO.setCategory("Test Category");
        updateDTO.setUnitPrice(10.0);
        updateDTO.setStock(100);

        Product product = new Product();
        product.setId(productId);
        product.setName(updateDTO.getName());
        product.setStock(updateDTO.getStock());
        product.setUnitPrice(updateDTO.getUnitPrice());

        when(productService.updateProduct(anyLong(), any(ProductUpdateDTO.class))).thenReturn(product);

        mockMvc.perform(put("/api/product/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value(updateDTO.getName()));

        verify(productService, times(1)).updateProduct(anyLong(), any(ProductUpdateDTO.class));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Long productId = 1L;

        doNothing().when(productService).deleteProduct(productId);

        mockMvc.perform(delete("/api/product/{id}", productId))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    public void testListProducts() throws Exception {
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product 1");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product 2");

        when(productService.listProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(product1.getId()))
                .andExpect(jsonPath("$[0].name").value(product1.getName()))
                .andExpect(jsonPath("$[1].id").value(product2.getId()))
                .andExpect(jsonPath("$[1].name").value(product2.getName()));

        verify(productService, times(1)).listProducts();
    }

    @Test
    public void testGetProductById() throws Exception {
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");

        when(productService.getProductForId(productId)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/product/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value(product.getName()));

        verify(productService, times(1)).getProductForId(productId);
    }

    @Test
    public void testGetProductByName() throws Exception {
        String productName = "Test Product";
        Product product = new Product();
        product.setId(1L);
        product.setName(productName);

        when(productService.searchProductsByName(productName)).thenReturn(Arrays.asList(product));

        mockMvc.perform(get("/api/product/search").param("nombre", productName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(product.getId()))
                .andExpect(jsonPath("$[0].name").value(product.getName()));

        verify(productService, times(1)).searchProductsByName(productName);
    }
}
