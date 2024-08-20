package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.UserRepository;
import com.OlegKulikov.pastbinclone.try_1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {return userRepository.findById(id);
    }
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
