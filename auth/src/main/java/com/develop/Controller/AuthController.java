package com.develop.Controller;

import com.develop.Model.Roles;
import com.develop.Model.Users;
import com.develop.Repository.RoleRepository;
import com.develop.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<Users> create(@RequestBody Users users){
        return ResponseEntity.ok(userRepository.save(users));
    }

    @DeleteMapping("/delete-by-username/{username}")
    public ResponseEntity<Users> deleteByUsername(@PathVariable String username){
        userRepository.delete(userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find-by-username/{username}")
    public ResponseEntity<Users> findByUserName(@PathVariable String username){
        return ResponseEntity.ok(userRepository.findByUsername(username)
                .orElseThrow(EntityNotFoundException::new));
    }

    @PatchMapping("/update-user")
    public ResponseEntity<Users> updateUser(@RequestBody Users users){
        return ResponseEntity.ok(userRepository.save(changeUser(users)));
    }

    @PatchMapping("/add-role/{name}/{roleName}")
    public ResponseEntity<Users> addRole(@PathVariable String name, @PathVariable String roleName){
        Users users = userRepository.findByUsername(name).orElseThrow(EntityNotFoundException::new);
        Roles role = roleRepository.findRolesByName(roleName).orElseThrow(EntityNotFoundException::new);
        users.getRoles().add(role);
        return ResponseEntity.ok(userRepository.save(users));
    }

    @PostMapping("/create-role")
    public ResponseEntity<Roles> createRole(@RequestBody Roles roles){
        return ResponseEntity.ok(roleRepository.save(roles));
    }

    @DeleteMapping("/delete-by-name/{name}")
    public ResponseEntity<Roles> deleteRole(@PathVariable String name){
        roleRepository.delete(roleRepository.findRolesByName(name).orElseThrow(EntityNotFoundException::new));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/find-by-name/{name}")
    public ResponseEntity<Roles> findByName(@PathVariable String name){
        return ResponseEntity.ok(roleRepository.findRolesByName(name).orElseThrow(EntityNotFoundException::new));
    }

    private Users changeUser(Users users){
        Users old = userRepository.findById(users.getId()).orElse(new Users());
        Optional.ofNullable(users.getId()).ifPresent(old::setId);
        Optional.ofNullable(users.getUsername()).ifPresent(old::setUsername);
        Optional.ofNullable(users.getFullName()).ifPresent(old::setFullName);
        Optional.ofNullable(users.getRoles()).ifPresent(old::setRoles);
        //add password encoder before
        Optional.ofNullable(users.getPassword()).ifPresent(old::setPassword);
        return old;
    }

}
