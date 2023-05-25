package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserRESTController {
    @Autowired
    private UserServiceImpl userService;

    private boolean hasAdminRole() {
        Set<Role> userRoles = getAuthUser().getRoles();
        return userRoles.contains(userService.getRole("ADMIN"));
    }

    private String errorResponse(HttpStatus status) {
        String errorHtml =
                "<header>"
                        + "<script>window.location.replace('https://http.cat/"+status.value()+"'); </script>"
                        + "</header>";
        return errorHtml;
    }

    @GetMapping("/users")
    public ResponseEntity<?> showAllUsers() {
        if (hasAdminRole()) {
            return new ResponseEntity<>(userService.getUsersList(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(errorResponse(HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> showUser(@PathVariable long id) {
        if (getAuthUser().getId() == id || hasAdminRole()) {
            User user = userService.getUser(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(errorResponse(HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
    }

    @GetMapping("/currentuser")
    public ResponseEntity<?> getAuthUser(@AuthenticationPrincipal UserDetails currentUser) {
        return new ResponseEntity<>(userService.getUser(currentUser.getUsername()), HttpStatus.OK);
    }

    private User getAuthUser() { //внутренний метод контроллера
        UserDetails userInfo = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userService.getUser(userInfo.getUsername());
    }

    @PostMapping("/users")
    public ResponseEntity<?> addNewUser(@RequestBody User user) {
        if (hasAdminRole()) {
            userService.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(errorResponse(HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        if (hasAdminRole()) {
            userService.updateUser(user.getId(), user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        return new ResponseEntity<>(errorResponse(HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        if (hasAdminRole()) {
            userService.deleteUser(id);
            return new ResponseEntity<>("DELETED!", HttpStatus.OK);
        }
        return new ResponseEntity<>(errorResponse(HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
    }
}

