package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.*;
import com.OlegKulikov.pastbinclone.try_1.model.*;
import com.OlegKulikov.pastbinclone.try_1.services.AppService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserWebController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TextRepository textRepository;
    private AppService service;

    @GetMapping("/home")
    public String welcome(){
        return "Welcome to the unprotected page";
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getAllUsers(Model model) {
        List<User> users = userRepository.findAll(); // Получаем всех пользователей из базы данных
        model.addAttribute("users", users); // Добавляем пользователей в модель
        return "users"; // Возвращаем имя HTML-шаблона
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String getUserPage(@PathVariable("id") int id, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        List<Text> texts = textRepository.findByUserId(id);
        model.addAttribute("user", currentUser);
        model.addAttribute("texts", texts);
        return "user_page";
    }



    @PostMapping("/new_user")
    public String addUser(@RequestBody User user) {
        service.addUser(user);
        return "User is saved";
    }
}