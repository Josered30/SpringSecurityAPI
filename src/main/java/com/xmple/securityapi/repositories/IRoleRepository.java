package com.xmple.securityapi.repositories;

import java.util.Optional;

import com.xmple.securityapi.entities.Role;
import com.xmple.securityapi.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}