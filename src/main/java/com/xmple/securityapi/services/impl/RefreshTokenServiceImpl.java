package com.xmple.securityapi.services.impl;

import com.xmple.securityapi.entities.RefreshToken;
import com.xmple.securityapi.repositories.IRefreshTokenRepository;
import com.xmple.securityapi.services.IRefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RefreshTokenServiceImpl implements IRefreshTokenService {


    @Autowired
    private IRefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken save(RefreshToken refreshToken) throws Exception {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void deleteById(Integer id) throws Exception {
        return;
    }

    @Override
    public Optional<RefreshToken> findById(Integer id) throws Exception {
        return Optional.empty();
    }

    @Override
    public List<RefreshToken> findAll() throws Exception {
        return null;
    }


    @Override
    public Optional<RefreshToken> findById(String id) throws Exception {
        return refreshTokenRepository.findById(id);
    }
}
