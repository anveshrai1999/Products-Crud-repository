package com.ProductsProject.ProductCrudApplication.Service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ProductsProject.ProductCrudApplication.Repository.ProductRepository;
import com.ProductsProject.ProductCrudApplication.model.Product;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	public Product createProduct(Product product) {
		log.info("Creating product: {}", product);
		try {
			return productRepository.save(product);
		} catch (Exception e) {
			log.error("Failed to create product: {}", e.getMessage());
			throw new RuntimeException("Failed to create product.", e);
		}
	}

	public Product getProductById(Long productId) {
		log.info("Fetching product with ID: {}", productId);
		return productRepository.findById(productId).orElse(null);
	}

	public Product updateProduct(Long productId, Product productDetails) {
		log.info("Updating product with ID: {}", productId);
		Product existingProduct = getProductById(productId);
		if (existingProduct == null) {
			log.warn("Product with ID {} not found", productId);
			throw new RuntimeException("Product with ID not found");
		}
		BeanUtils.copyProperties(productDetails, existingProduct);
		return productRepository.save(existingProduct);
	}

	public void deleteProduct(Long productId) {
		log.info("Deleting product with ID: {}", productId);
		if (productRepository.existsById(productId)) {
			productRepository.deleteById(productId);
			log.info("Product deleted successfully");
		} else {
			log.error("Failed to delete product with ID: {}", productId);
			throw new RuntimeException("Failed to delete product with ID: " + productId);
		}
	}

	public Product applyDiscountOrTax(Long productId, Double discountPercentage, Double taxRate) {
		Optional<Product> optionalProduct = productRepository.findById(productId);
		if (optionalProduct.isPresent()) {
			Product product = optionalProduct.get();
			if (discountPercentage != null) {
				applyDiscount(product, discountPercentage);
			} else if (taxRate != null) {
				applyTax(product, taxRate);
			} else {
				throw new IllegalArgumentException("Either discount percentage or tax rate must be provided.");
			}
			return productRepository.save(product);
		} else {
			throw new IllegalArgumentException("Product with ID " + productId + " not found.");
		}
	}

	private void applyDiscount(Product product, Double discountPercentage) {
		log.info("Applying discount of {}% to product with ID: {}", discountPercentage, product.getProductId());
		double discountAmount = product.getPrice() * (discountPercentage / 100);
		double discountedPrice = product.getPrice() - discountAmount;
		product.setPrice(discountedPrice);
		log.info("Discount applied successfully to product with ID: {}", product.getProductId());
	}

	private void applyTax(Product product, Double taxRate) {
		log.info("Applying tax of {}% to product with ID: {}", taxRate, product.getProductId());
		double taxAmount = product.getPrice() * (taxRate / 100);
		double taxedPrice = product.getPrice() + taxAmount;
		product.setPrice(taxedPrice);
		log.info("Tax applied successfully to product with ID: {}", product.getProductId());
	}

}
