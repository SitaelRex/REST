package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;


@RestController
    @RequestMapping("/api")
    public class UserRESTController {
        @Autowired
        private UserServiceImpl userService;

        @GetMapping("/users")
        public List<User> showAllUsers() {
            List<User> allUsers = userService.getUsersList();
            return allUsers;
        }

        @GetMapping("/users/{id}")
        public User showUser(@PathVariable long id) {
            User user = userService.getUser(id);
            return user;
        }

        @PostMapping("/users")
        public User addNewUser(@RequestBody User user) {
            userService.saveUser(user);
            return user;
        }

         @PutMapping("/users")
         public User updateUser(@RequestBody User user) {
             userService.updateUser(user.getId(), user);
             return user;
         }
        @DeleteMapping("/users/{id}")
         public String deleteUser(@PathVariable long id){
            userService.deleteUser(id);
            return "DELETED!";

         }
    }

