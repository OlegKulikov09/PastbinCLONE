package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.UserRepository;
import com.OlegKulikov.pastbinclone.try_1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // Проверка уникальности логина
        Optional<User> existingUser = userRepository.findByLogin(user.getLogin());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Login already exists.");
        }

        // Проверка формата email
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format.");
        }

        return userRepository.save(user);
    }
    @GetMapping("/checkLogin")
    public Map<String, Boolean> checkLoginUnique(@RequestParam String login) {
        boolean isUnique = userRepository.findByLogin(login).isEmpty();
        Map<String, Boolean> response = new HashMap<>();
        response.put("isUnique", isUnique);
        return response;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {return userRepository.findById(id);
    }
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
