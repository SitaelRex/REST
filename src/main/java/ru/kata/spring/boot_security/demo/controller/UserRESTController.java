package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class UserRESTController {

    private static final int ADMIN_ROLE_ID = 1;
    @Autowired
    private UserServiceImpl userService;


    private boolean hasAdminRole() {
        Set<Role> userRoles = getAuthUser().getRoles();
        return userRoles.contains(userService.getRoles().get(ADMIN_ROLE_ID));
    }

    @GetMapping("/users")
    public List<User> showAllUsers() {
        if (hasAdminRole()) {
            return userService.getUsersList();
        } else {
            return new ArrayList<User>();
        }
    }

    @GetMapping("/users/{id}")
    public User showUser(@PathVariable long id) {
        if (getAuthUser().getId() == id || hasAdminRole()) {
            User user = userService.getUser(id);
            return user;
        }
        return null;
    }

    @GetMapping("/currentuser")
    public User getAuthUser(@AuthenticationPrincipal UserDetails currentUser) {
        return userService.getUser(currentUser.getUsername());
    }

    private User getAuthUser() {
        UserDetails userInfo = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userService.getUser(userInfo.getUsername());
    }

    @PostMapping("/users")
    public User addNewUser(@RequestBody User user) {
        if (hasAdminRole()) {
            userService.saveUser(user);
            return user;
        }
        return null;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        if (hasAdminRole()) {
            userService.updateUser(user.getId(), user);
            return user;
        }
        return null;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable long id) {
        if (hasAdminRole()) {
            userService.deleteUser(id);
            return "DELETED!";
        }
        return "ACCESs DENIED";
    }
}

