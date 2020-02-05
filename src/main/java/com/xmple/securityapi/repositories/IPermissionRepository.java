package com.xmple.securityapi.repositories;

import com.xmple.securityapi.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IPermissionRepository  extends JpaRepository<Permission,Long> {
    Optional<Permission> findByName(PermissionName permissionName);
}
