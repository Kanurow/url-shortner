package com.rowland.engineering.shortner.repository;


import com.rowland.engineering.shortner.entity.Role;
import com.rowland.engineering.shortner.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);
}
