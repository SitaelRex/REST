package ru.kata.spring.boot_security.demo.dao;


import  ru.kata.spring.boot_security.demo.model.User;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository

public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    public UserDaoImpl() {

    }

    @Override
    public User getUser(long id) {
        String hql = "FROM User user WHERE user.id = :userId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", id);
        List results = query.getResultList();

        if (results != null && !results.isEmpty()) {
            return (User) results.get(0);
        }
        return null;
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void deleteUser(long id) {
        String hql = "DELETE FROM User WHERE id = :userId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", id);
        query.executeUpdate();
    }

    @Override
    public List<User> getUsersList() {
        String hql = "FROM User";
        Query query = entityManager.createQuery(hql);
        return query.getResultList();
    }

    @Override
    public void updateUser(long id, User newUser) {
        User user = getUser(id);
        entityManager.detach(user);
        user.setEmail(newUser.getEmail());
        user.setGroupId(newUser.getGroupId());
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        entityManager.merge(user);
    }
}
