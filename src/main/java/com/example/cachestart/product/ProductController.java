package com.example.cachestart.product;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/{productName}")
    public Product getProductByName(@PathVariable String productName) {
        return productService.getProduct(productName);
    }

    @DeleteMapping("/products/{productName}")
    public void deleteCacheProductByName(@PathVariable String productName) {
        Product product = new Product(productName);
        productService.refreshProduct(product);
    }

    @DeleteMapping("/products")
    public void deleteCacheAllProduct() {
        productService.refreshAllProducts();
    }

}
