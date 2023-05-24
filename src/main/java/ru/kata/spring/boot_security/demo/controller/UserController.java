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
public class UserController { //не понял как передать информацию об аутентефикации на страницу без модели, поэтому

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String redirectUser(ModelMap model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.getUser(currentUser.getUsername());
        model.addAttribute("currentUser", user);
        return "users/index";
    }

    @GetMapping("/admin")
    public String indexUsers(ModelMap model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.getUser(currentUser.getUsername());
        model.addAttribute("currentUser", user);
        return "users/index";
    }
}
