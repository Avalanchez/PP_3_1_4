package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserDao;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        userDao.saveUser(user);
    }

    @Override
    public User getUserById(Long id) {
        return this.userDao.getUserById(id);
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        this.userDao.updateUser(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        this.userDao.deleteUserById(id);
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
