package com.develop.Controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.develop.Model.Roles;
import com.develop.Model.Users;
import com.develop.Repository.RoleRepository;
import com.develop.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/create")
    public ResponseEntity<Users> create(@RequestBody Users users){
        users.setPassword(passwordEncoder.encode(users.getPassword()));
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
        List<Roles> roles = users.getRoles();
        if (Objects.isNull(roles)) {
            roles = new ArrayList<>();
            users.setRoles(roles);
        }
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
        Optional.ofNullable(passwordEncoder.encode(users.getPassword())).ifPresent(old::setPassword);
        return userRepository.save(old);
    }


    private void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                Users user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+1000 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles()
                                .stream().map(Roles::getName)
                                .collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else{
            throw new RuntimeException("Refresh token is missing");
        }
            }
}



