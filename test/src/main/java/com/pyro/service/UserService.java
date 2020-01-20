package com.pyro.service;

import com.pyro.entities.User;

import java.util.List;

public interface UserService {
    void save(User user);

    List<User> findAll();

    User findByUsername(String username);
}