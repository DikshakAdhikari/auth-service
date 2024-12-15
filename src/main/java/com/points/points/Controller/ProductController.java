package com.points.points.Controller;

import com.points.points.Configuration.SecurityConfig;
import com.points.points.Service.JwtService;
import com.points.points.Service.ProductService;
import com.points.points.dto.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    SecurityConfig securityConfig;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;
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

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        //Before we allow to get the token we need to authenticate that particular user
        //As we know from spring security flow from filter request will delegate to the authentication manager for authentication, so we autowire/inject authentication manager
        //So we use AuthenticationManger to validate the user, if it's the correct user then only give correct token

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if(authenticate.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid User Request !");
        }
    }
}
