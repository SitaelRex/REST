package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);

    void deleteUser(long id);

    User getUser(long id);

    List<User> getUsersList();

    void updateUser(long id, User user);
}
