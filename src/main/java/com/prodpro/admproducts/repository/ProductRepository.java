package com.prodpro.admproducts.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.prodpro.admproducts.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String name);
}