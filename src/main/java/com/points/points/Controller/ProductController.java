package com.points.points.Controller;

import com.points.points.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
   @Autowired
    ProductService productService;


    @GetMapping("/all")
    public String getAllProducts() {
        return productService.getAllProducts();
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{productId}")
    public String getProductWithId(
            @PathVariable Long productId
    ) {
        return productService.getProductWithId(productId);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/Welcome")
    public String getWelcome() {
        return productService.getWelcome();
    }
}
