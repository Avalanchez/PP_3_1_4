package ru.kata.spring.boot_security.demo.repository;


import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserDao {
    void saveUser(User user);
    User getUserById(int id);
    void updateUser(User user);
    void deleteUserById(int id);
    List<User> getAllUsers();
    User findByUsername(String name);
}
