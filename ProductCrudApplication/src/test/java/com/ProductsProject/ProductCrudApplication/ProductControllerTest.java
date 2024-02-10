package com.ProductsProject.ProductCrudApplication;

import com.ProductsProject.ProductCrudApplication.Controller.ProductController;
import com.ProductsProject.ProductCrudApplication.Service.ProductService;
import com.ProductsProject.ProductCrudApplication.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductControllerTest {

    @Mock
    ProductService productService;

    @InjectMocks
    ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(50.0);
        product.setQuantityAvailable(100L);
        Mockito.when(productService.createProduct(product)).thenReturn(product);
        ResponseEntity<?> responseEntity = productController.createProduct(product);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(product, responseEntity.getBody());
    }

    @Test
    void testGetProductById() {
        Long productId = 1L;
        Product product = new Product();
        product.setProductId(productId);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(50.0);
        product.setQuantityAvailable(100L);
        Mockito.when(productService.getProductById(productId)).thenReturn(product);
        ResponseEntity<?> responseEntity = productController.getProductById(productId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(product, responseEntity.getBody());
    }


    @Test
    void testUpdateProduct() {
        Long productId = 1L;
        Product product = new Product();
        product.setProductId(productId);
        product.setName("Updated Product");
        product.setDescription("Updated Description");
        product.setPrice(60.0);
        product.setQuantityAvailable(120L);
        ResponseEntity<?> responseEntity = productController.updateProduct(product);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Product updated successfully", responseEntity.getBody());
    }

    @Test
    void testDeleteProduct() {
        Long productId = 1L;
        ResponseEntity<String> responseEntity = productController.deleteProduct(productId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Product deleted successfully", responseEntity.getBody());
    }

    @Test
    void testApplyDiscountOrTax() {
        Long productId = 1L;
        Double discountPercentage = 10.0;
        Double taxRate = null;
        Product product = new Product();
        product.setProductId(productId);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(50.0);
        product.setQuantityAvailable(100L);
        Mockito.when(productService.applyDiscountOrTax(productId, discountPercentage, taxRate)).thenReturn(product);
        ResponseEntity<?> responseEntity = productController.applyDiscountOrTax(productId, discountPercentage, taxRate);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(product, responseEntity.getBody());
    }

}

