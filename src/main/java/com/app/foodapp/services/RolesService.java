package com.app.foodapp.services;

import com.app.foodapp.models.Roles;
import com.app.foodapp.models.Users;
import com.app.foodapp.repositories.RolesRepository;
import com.app.foodapp.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesService {

    @Autowired
    private UsersRepository usersRepository;

    private RolesRepository rolesRepository;

    public List<Roles> getRoles() {
        return this.rolesRepository.findAll();
    }

    public Optional<Roles> getUserById(Long id) {
        return this.rolesRepository.findById(id);
    }

    public Roles createRole(Roles role) {

        if (this.usersRepository.findByEmail(role.getName()).isPresent()) {
            throw new RuntimeException("El Nombre ya existe.");
        }

        Roles newRole = new Roles();

        newRole.setName(role.getName());
        newRole.setRoute(role.getRoute());
        newRole.setDescription(role.getDescription());
        newRole.setImage(role.getImage());


        return this.rolesRepository.save(newRole);
    }

    }

