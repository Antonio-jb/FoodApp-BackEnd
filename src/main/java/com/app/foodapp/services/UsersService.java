package com.app.foodapp.services;

import com.app.foodapp.models.Roles;
import com.app.foodapp.models.Users;
import com.app.foodapp.repositories.RolesRepository;
import com.app.foodapp.repositories.UsersRepository;
import com.app.foodapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public List<Users> getAllUsers() {
        return this.usersRepository.findAll();
    }

    public Optional<Users> getUserById(Long id) {
        return this.usersRepository.findById(id);
    }

    public Optional<Users> getUserByEmail(String email) {
        return this.usersRepository.findByEmail(email);
    }

    public void deleteUserById(Long id) {
        Users userOptional = this.usersRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Usuario no encontrado con ID: " + id));
        this.usersRepository.delete(userOptional);
    }

    public Users updateUser(Users user, Long id) {
        Users userOptional = this.usersRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Usuario no encontrado con ID: " + id));

        userOptional.setFirstName(user.getFirstName());
        userOptional.setLastName(user.getLastName());
        userOptional.setEmail(user.getEmail());
        userOptional.setPhone(user.getPhone());
        userOptional.setImage(user.getImage());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            userOptional.setPassword(user.getPassword());
        }

        return this.usersRepository.save(userOptional);
    }

    public Users createUser(Users user) {

        if (this.usersRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya existe.");
        }

        Users newUser = new Users();

        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPhone(user.getPhone());
        newUser.setEmail(user.getEmail());

        newUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        newUser.setImage("");

        Set<Roles> roles = new HashSet<>();
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Roles defaultRole = this.rolesRepository.findRoleByName("Admin");
            if (defaultRole != null) {
                roles.add(defaultRole);
            } else {
                throw new RuntimeException("No se ha podido agregar el rol");
            }
        }
            else
            {
                for (Roles role : user.getRoles()) {
                    Roles newRole = this.rolesRepository.findRoleByName(role.getName());
                    roles.add(newRole);
                }
            }
        newUser.setRoles(roles);
        return this.usersRepository.save(newUser);
    }

    public String login (String email, String password) {

        //Users optionalUser = this.usersRepository.findByEmail(email).orElseThrow(() ->
        //        new RuntimeException("El usuario no existe."));

        Optional<Users> optionalUser = this.usersRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "No existe";
        }
        /*
         * usuario = {
         * firstName: Antonio
         * lastName: Jimenez
         * email: antonio@gmail.com
         * password 12345
         * ..
         *}
         */
        Users user = optionalUser.get();
        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            return "Password incorrect";
        }
        else {
            return "Login successful";
        }
    }

    public String createToken(String email) {
        return this.jwtUtil.generateToken(email);
    }

}
