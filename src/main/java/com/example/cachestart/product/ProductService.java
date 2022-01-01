package com.example.cachestart.product;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Cacheable("product") // 캐시명 지정
    public Product getProduct(String productName) {
        System.out.println("product productName : " + productName);

        Product product = new Product(productName);
        return product;
    }

    @CacheEvict(value = "product", key = "#product?.productName")
    public void refreshProduct(Product product) {
        System.out.println("refresh product cache productName : " + product.getProductName());
    }

    @CacheEvict(value = "product", allEntries = true)
    public void refreshAllProducts() {
        System.out.println("refresh product cache All");
    }

}
