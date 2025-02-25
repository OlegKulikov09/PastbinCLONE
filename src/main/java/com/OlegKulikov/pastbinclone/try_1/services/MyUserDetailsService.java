package com.OlegKulikov.pastbinclone.try_1.services;

import com.OlegKulikov.pastbinclone.try_1.Repositories.UserRepository;
import com.OlegKulikov.pastbinclone.try_1.model.User;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        Hibernate.initialize(user.getTexts());
        Set<GrantedAuthority> grantedAuthorities = Collections.singleton(
                new SimpleGrantedAuthority(user.getRole())  // Используем строку в качестве роли
        );

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                grantedAuthorities
        );
    }
    public List<User> allUsers() {
        return userRepository.findAll();
    }
    public boolean saveUser(User user) {
        Optional<User> userFromDB = userRepository.findById(user.getId());
        if (userFromDB.isPresent()) {
            return false;
        }
        user.setRole("ROLE_USER");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }
    public void deleteUser(int id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }
}
