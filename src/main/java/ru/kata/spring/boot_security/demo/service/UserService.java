package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.Optional;

import java.util.List;

public interface UserService {
    void deleteUser(User user);

    Optional<User> getUserById(Long id);

    void updateUser(User user);

    void createUser(User user);

    Iterable<User> getAllUsers();

    List<Role> getRoles();

}
