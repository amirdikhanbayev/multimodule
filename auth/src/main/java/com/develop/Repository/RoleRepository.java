package com.develop.Repository;

import com.develop.Model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findRolesByName(String name);
}
