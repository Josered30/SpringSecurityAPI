package com.xmple.securityapi.services.impl;

import com.xmple.securityapi.entities.User;
import com.xmple.securityapi.repositories.IUserRepository;
import com.xmple.securityapi.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

    @Autowired
    IUserRepository userRepository;

    @Transactional
    @Override
    public User save(User user) throws Exception {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void deleteById(Integer id) throws Exception {
        userRepository.deleteById((long)id);
    }

    @Override
    public Optional<User> findById(Integer id) throws Exception {
        return userRepository.findById((long)id);
    }

    @Override
    public List<User> findAll() throws Exception {
        return userRepository.findAll();
    }
}
