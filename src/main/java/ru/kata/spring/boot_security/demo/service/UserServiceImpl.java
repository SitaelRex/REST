package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;


import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserDao dao;

    @Autowired
    public UserServiceImpl(UserDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        dao.saveUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        dao.deleteUser(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(long id) {
        return dao.getUser(id);
    }

    @Override
    @Transactional
    public void updateUser(long id, User user) {
        dao.updateUser(id, user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersList() {
        return dao.getUsersList();
    }
}
