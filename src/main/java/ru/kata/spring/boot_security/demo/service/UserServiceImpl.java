package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService {
    @PersistenceContext
    private EntityManager entityManager;
    UserRepository userRepository;
    RoleRepository roleRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    @Lazy
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        //ручная инициализация ролей в БД для связи с пользователями
        roleRepository.save(new Role(1L, "ROLE_USER"));
        roleRepository.save(new Role(2L, "ROLE_ADMIN"));
    }

    @Transactional
    public void saveUser(ru.kata.spring.boot_security.demo.model.User user) {
        ru.kata.spring.boot_security.demo.model.User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return;
        }
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    public ru.kata.spring.boot_security.demo.model.User getUser(long id) {
        Optional<ru.kata.spring.boot_security.demo.model.User> userFromDb = userRepository.findById(id);
        return userFromDb.orElse(new ru.kata.spring.boot_security.demo.model.User());
    }

    @Transactional(readOnly = true)
    public ru.kata.spring.boot_security.demo.model.User getUser(String username) {
        ru.kata.spring.boot_security.demo.model.User userFromDb = userRepository.findByUsername(username);
        return userFromDb;
    }

    @Transactional
    public void updateUser(long id, ru.kata.spring.boot_security.demo.model.User newUser) {
        ru.kata.spring.boot_security.demo.model.User userFromDb = getUser(id);
        entityManager.detach(userFromDb);
        userFromDb.setEmail(newUser.getEmail());
        userFromDb.setGroupId(newUser.getGroupId());
        userFromDb.setFirstName(newUser.getFirstName());
        userFromDb.setLastName(newUser.getLastName());
        entityManager.merge(userFromDb);
    }

    @Transactional(readOnly = true)
    public List<ru.kata.spring.boot_security.demo.model.User> getUsersList() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ru.kata.spring.boot_security.demo.model.User myUser = userRepository.findByUsername(username);

        if (myUser == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Role[] rolesOfUser = myUser.getRoles().toArray(new Role[myUser.getRoles().size()]);

        UserDetails user = User.builder()
                .username(myUser.getUsername())
                .password(myUser.getPassword())
                .roles(Arrays.stream(rolesOfUser)
                        .map(Object::toString)
                        .toArray(String[]::new))
                .build();

        return user;
    }
}
