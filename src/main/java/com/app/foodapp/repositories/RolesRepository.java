package com.app.foodapp.repositories;

import com.app.foodapp.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
* Jpa nos facilita funciones CRUD por defecto.
* CREATE, READ, UPDATE, REPEAT.
*
*/

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    Roles findRoleByName(String name);

}
