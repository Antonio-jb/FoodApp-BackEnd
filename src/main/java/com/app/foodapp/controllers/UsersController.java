package com.app.foodapp.controllers;

import com.app.foodapp.dto.LoginRequest;
import com.app.foodapp.models.Users;
import com.app.foodapp.services.UsersService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/*
* Localhost:8080
*
* context-path de application.properties la URL queda: localhost:8080/api
*
* En este controlador la URL es: localhost:8080/api/users
*/
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

 //  @GetMapping("/create")
 //  public ResponseEntity<Users> createUser() {
 //      Users user = new Users();

 //      user.setFirstName("Antonio");
 //      user.setLastName("Jimenez");
 //      user.setEmail("aajb@outlook.com");
 //      user.setPassword("12345");
 //      user.setImage("Hola.png");
 //      user.setPhone("123456789");

 //      return ResponseEntity.ok(user);
 //  }

    // localhost:8080/api/users -> Si no hay parametro en GetMapping
    // localhost:8080/api/users/get-users
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = this.usersService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // localhost:8080/api/users/create
    @PostMapping("/create")
    public ResponseEntity<Users> createUser(@RequestBody Users user)  {
        Users createUser = this.usersService.createUser(user);
        return ResponseEntity.ok(createUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest credentials) {

        String response = this.usersService.login(credentials.getEmail(), credentials.getPassword());
        switch (response) {
            case "No existe": {
                return ResponseEntity.status(404).body(Collections.singletonMap("message", "No existe"));
            }
            case "Password incorrect": {
                return ResponseEntity.status(401).body(Collections.singletonMap("message", "Password incorrect"));
            }
            case "Login successful": {
                String token = this.usersService.createToken(credentials.getEmail());
                return ResponseEntity.status(200).body(Collections.singletonMap("message", token));
            }
            default: {
                return ResponseEntity.status(500).body(Collections.singletonMap("message", "Server error"));
            }
        }
    }
}
