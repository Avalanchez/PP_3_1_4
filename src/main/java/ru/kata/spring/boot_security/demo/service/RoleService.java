package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Set;
import java.util.List;

public interface RoleService {
    void createRoles(Set<Role> roles);

    Set<Role> getAllRoles();

    Set<Role> getRolesByIds(List<Long> ids);
}