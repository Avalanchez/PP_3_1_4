package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin";
    }

    @GetMapping("/new")
    public String createUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("listRoles", userService.getRoles());
        return "create_user";
    }

    @PostMapping()
    public String saveUser(@ModelAttribute("user") User user, @RequestParam("roles") List<Integer> roleIds) {
        user.setRoles(userService.getRolesByIds(roleIds));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUserById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("listRoles", userService.getRoles());
        return "edit_user";
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") User user,
                             @RequestParam("roles") List<Integer> roleIds) {
        user.setRoles(userService.getRolesByIds(roleIds));
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}