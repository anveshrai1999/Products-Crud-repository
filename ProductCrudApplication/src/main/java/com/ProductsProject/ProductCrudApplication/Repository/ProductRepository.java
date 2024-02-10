package com.ProductsProject.ProductCrudApplication.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ProductsProject.ProductCrudApplication.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
