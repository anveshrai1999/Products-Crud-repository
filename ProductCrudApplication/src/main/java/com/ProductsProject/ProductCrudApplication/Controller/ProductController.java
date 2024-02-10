package com.ProductsProject.ProductCrudApplication.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ProductsProject.ProductCrudApplication.Service.ProductService;
import com.ProductsProject.ProductCrudApplication.model.Product;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	ProductService productService;

	@PostMapping
	public ResponseEntity<?> createProduct(@RequestBody Product product) {
		log.info("POST /api/products");
		try {
			Product createdProduct = productService.createProduct(product);
			return new ResponseEntity<Product>(createdProduct, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Failed to create product: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable Long productId) {
		log.info("GET /api/products/{}", productId);
		Product product = productService.getProductById(productId);
		if (Objects.isNull(product)) {
			log.warn("Product with ID {} not found", productId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product with ID not found");
		}
		return ResponseEntity.ok(product);
	}

	@PutMapping
	public ResponseEntity<?> updateProduct(@RequestBody Product product) {
		log.info("PUT /api/product");
		Long productId = product.getProductId();
		if (productId == null) {
			log.error("Product ID is required for update");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product ID is required for update");
		}
		try {
			productService.updateProduct(productId, product);
			return ResponseEntity.status(HttpStatus.OK).body("Product updated successfully");
		} catch (Exception e) {
			log.error("Failed to update product: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update product");
		}
	}

	@DeleteMapping("/{productId}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
		log.info("DELETE /api/products/{}", productId);
		try {
			productService.deleteProduct(productId);
			return ResponseEntity.ok("Product deleted successfully");
		} catch (Exception e) {
			log.error("Failed to delete product: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to delete product: " + e.getMessage());
		}
	}

	@PostMapping("/{productId}/apply")
	public ResponseEntity<?> applyDiscountOrTax(@PathVariable Long productId,
			@RequestParam(required = false) Double discountPercentage, @RequestParam(required = false) Double taxRate) {
		try {
			log.info("Applying discount or tax to product with ID: {}", productId);
			Product updatedProduct = productService.applyDiscountOrTax(productId, discountPercentage, taxRate);
			log.info("Discount or tax applied successfully to product with ID: {}", productId);
			return ResponseEntity.ok(updatedProduct);
		} catch (Exception e) {
			log.error("Failed to apply discount or tax to product with ID: {}", productId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
