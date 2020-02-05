package com.xmple.securityapi.repositories;


import com.xmple.securityapi.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshToken,String> {
    public Optional<RefreshToken> findById(String id);

}
