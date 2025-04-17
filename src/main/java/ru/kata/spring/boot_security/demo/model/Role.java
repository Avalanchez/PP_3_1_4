package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @Column (name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column (name = "role")
    private String roleName;

    public Role() {
    }
    public Role(int id) {
        this.id = id;
    }

    public Role(int id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }
    public Role(String name) {
        this.roleName = name;
    }
    @Override
    public String getAuthority() {
        return getRoleName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public String toString() {
        return this.roleName;
    }
}
