package com.points.points.Controller;

import com.points.points.Entity.User;
import com.points.points.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {
 @Autowired
    UserService userService;
    @PostMapping
    public ResponseEntity<String> registerUser(
            @RequestBody User user
            ) {
        return ResponseEntity.ok(userService.registerUser(user));
    }
}
