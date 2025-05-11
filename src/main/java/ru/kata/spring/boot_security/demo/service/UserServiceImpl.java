package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserDao;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.saveUser(user);
    }

    @Override
    public User getUserById(Long id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь с ID " + id + " не найден");
        }
        return user;
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        if (userDao.getUserById(user.getId()) == null) {
            throw new EntityNotFoundException("Невозможно обновить. Пользователь с ID " + user.getId() + " не найден");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.updateUser(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        User user = userDao.getUserById(id);
        if (user == null) {
            throw new EntityNotFoundException("Невозможно удалить. Пользователь с ID " + id + " не найден");
        }
        userDao.deleteUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userDao.getAllUsers();
    }

    @Override
    public UserDetails loadUserByUsername(String name) {
        return userDao.findByUsername(name);
    }
    @Override
    public List<Role> getRoles() {
        return userDao.getRoles();
    }

    @Override
    public Set<Role> getRolesByIds(List<Integer> ids) {
        return userDao.getRolesByIds(ids);
    }
}
