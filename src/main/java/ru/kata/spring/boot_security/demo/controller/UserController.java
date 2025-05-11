package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;


@Controller
public class UserController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public UserController(UserService userService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/admin")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin";
    }

    @GetMapping("/user")
    public String getSingleUser(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("user", userDetailsService.loadUserByUsername(username));
        return "/user";
    }

    @GetMapping ("/admin/new")
    public String createUser (@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("listRoles", userService.getRoles());
        return "create_user";
    }

    @PostMapping ("/admin")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam("roles") List<Integer> roleIds) {
        user.setRoles(userService.getRolesByIds(roleIds));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("admin/edit/{id}")
    public String editUserById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("listRoles", userService.getRoles());
        return "edit_user";
    }

    @PostMapping("/admin/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user, @RequestParam("roles") List<Integer> roleIds) {
        user.setRoles(userService.getRolesByIds(roleIds));
        userService.updateUser(user);
        return "redirect:/admin";
    }


    @GetMapping("/admin/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

}