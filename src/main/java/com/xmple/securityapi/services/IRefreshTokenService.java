package com.xmple.securityapi.services;


import com.xmple.securityapi.entities.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenService extends CrudService<RefreshToken> {
   Optional<RefreshToken> findById(String id) throws  Exception;

}
