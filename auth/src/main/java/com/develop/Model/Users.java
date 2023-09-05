package com.develop.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Data
@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String fullName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")}
            , inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Roles> roles;
}
