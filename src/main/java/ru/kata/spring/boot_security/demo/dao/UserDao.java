package ru.kata.spring.boot_security.demo.dao;




import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    User getUser(long id);

    public void saveUser(User user);

    void deleteUser(long id);

    List<User> getUsersList();

    void updateUser(long id, User newUser);
}
