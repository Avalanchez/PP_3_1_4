package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.Set;

import java.util.List;

public interface UserService extends UserDetailsService {
    void saveUser(User user);
    User getUserById(Long id);
    void updateUser(User user);
    void deleteUserById(Long id);
    List<User> getAllUsers();
    List<Role> getRoles();
    Set<Role> getRolesByIds(List<Integer> ids);
}
