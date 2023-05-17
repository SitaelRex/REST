package ru.kata.spring.boot_security.demo.controller;


import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;

@Controller
public class UserController {


    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }


    private long getCurrentUserId(UserDetails currentUser) {
        User user = userService.getUser(currentUser.getUsername());
        return user.getId();
    }

    @GetMapping("/user")
    public String redirectUser(ModelMap model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.getUser(currentUser.getUsername());
        model.addAttribute("currentUser", user);
        model.addAttribute("newuser", new User()); // без этого ломается (страница общая)
        //TODO сделать newuser опциональным
        return "users/index"; //"redirect:/user/"+getCurrentUserId(currentUser);
    }

    @GetMapping("/admin")
    public String indexUsers(ModelMap model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.getUser(currentUser.getUsername());
        model.addAttribute("users", userService.getUsersList());
        model.addAttribute("currentUser", user);
        model.addAttribute("newuser", new User());
        List<Role> roles = userService.getRoles();
        model.addAttribute("rolesList", roles);
        for (int i = 0; i < userService.getUsersList().size(); i++){
            model.addAttribute("user["+i+"]", userService.getUsersList().get(i));
        }
        return "users/index";
    }

    @GetMapping("/user/{id}")
    public String showUser(@PathVariable("id") int id, ModelMap model) {
        model.addAttribute("user", userService.getUser(id));
        return "users/show";
    }

    @GetMapping("/admin/new")
    public String newUser(ModelMap model) {
        model.addAttribute("user", new User());
        return "users/new";
    }

    @PostMapping("/admin")
    public String createUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String editUser(ModelMap model, @PathVariable("id") int id) {
        model.addAttribute("user", userService.getUser(id));
        return "users/edit";
    }

    @PatchMapping("/admin/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") int id) {
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
