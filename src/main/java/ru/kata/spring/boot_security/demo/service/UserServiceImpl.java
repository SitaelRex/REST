package ru.kata.spring.boot_security.demo.service;

import com.sun.xml.bind.v2.TODO;
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
//import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserDetailsService {
    @PersistenceContext
    private EntityManager entityManager;
    //private final UserDao dao;
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
        //roleRepository.save(new Role(1L, "ROLE_USER"));
        //roleRepository.save(new Role(2L, "ROLE_ADMIN"));
    }
    // {this.dao = dao;}

    @Transactional
    public void saveUser(ru.kata.spring.boot_security.demo.model.User user) {
        ru.kata.spring.boot_security.demo.model.User userFromDB = userRepository.findByUsername(user.getUsername());

        if (userFromDB != null) {
            return;
        }

        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        //dao.saveUser(user);
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
       // return dao.getUser(id);
    }

    @Transactional(readOnly = true)
    public ru.kata.spring.boot_security.demo.model.User getUser(String username) {
        ru.kata.spring.boot_security.demo.model.User userFromDb = userRepository.findByUsername(username);
        return userFromDb;//.orElse(new ru.kata.spring.boot_security.demo.model.User());
        // return dao.getUser(id);
    }

    @Transactional
    public void updateUser(long id, ru.kata.spring.boot_security.demo.model.User newUser) {
        ru.kata.spring.boot_security.demo.model.User userFromDb = getUser(id);
       // userFromDb = user;
        entityManager.detach(userFromDb);
        userFromDb.setEmail(newUser.getEmail());
        userFromDb.setGroupId(newUser.getGroupId());
        userFromDb.setFirstName(newUser.getFirstName());
        userFromDb.setLastName(newUser.getLastName());
        entityManager.merge(userFromDb);

        //dao.updateUser(id, user);
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
       // Arrays.stream(rolesOfUser).forEach(System.out::println);

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
