package com.points.points.Service;

import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public String getAllProducts() {
        return "Get all product";
    }

    public String getProductWithId(Long productId) {
        return String.format("Product with id %s found", productId);
    }

    public String getWelcome() {
        return "Welcome!!";
    }
}
