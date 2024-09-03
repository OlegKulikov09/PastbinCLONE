package com.OlegKulikov.pastbinclone.try_1;

import com.OlegKulikov.pastbinclone.try_1.Repositories.UserRepository;
import com.OlegKulikov.pastbinclone.try_1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // Используем Optional для поиска пользователя
        Optional<User> userOptional = userRepository.findByLogin(login);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with login: " + login);
        }

        User user = userOptional.get();
        // Возвращаем UserDetails для Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                new ArrayList<>() // Пустой список авторизаций для примера
        );
    }
}