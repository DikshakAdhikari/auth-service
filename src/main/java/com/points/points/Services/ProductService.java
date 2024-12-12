package com.points.points.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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
