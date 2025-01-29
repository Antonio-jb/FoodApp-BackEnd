package com.app.foodapp.controllers;

import com.app.foodapp.models.Roles;
import com.app.foodapp.models.Users;
import com.app.foodapp.services.RolesService;
import com.app.foodapp.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RolesController {
    @Autowired
    private RolesService rolesService;

    // localhost:8080/api/users -> Si no hay parametro en GetMapping
    // localhost:8080/api/users/get-users
    @GetMapping("get-roles")
    public ResponseEntity<List<Roles>> getRoles() {
        List<Roles> roles = this.rolesService.getRoles();
        return ResponseEntity.ok(roles);
    }

    // localhost:8080/api/users/create
    @PostMapping("/create")
    public ResponseEntity<Roles> createRole(@RequestBody Roles roles)  {
        Roles createRole = this.rolesService.createRole(roles);
        return ResponseEntity.ok(createRole);
    }

}
