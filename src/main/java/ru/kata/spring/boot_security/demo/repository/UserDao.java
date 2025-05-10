package ru.kata.spring.boot_security.demo.repository;


import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;
import java.util.Set;

public interface UserDao {
    void saveUser(User user);
    User getUserById(Long id);
    void updateUser(User user);
    void deleteUserById(Long id);
    List<User> getAllUsers();
    User findByUsername(String name);
    Role findRoleByRoleName(String name);
    List<Role> getRoles();
    Set<Role> getRolesByIds(List<Integer> ids);
}
