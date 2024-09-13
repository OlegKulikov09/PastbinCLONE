package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.*;
import com.OlegKulikov.pastbinclone.try_1.model.*;
import com.OlegKulikov.pastbinclone.try_1.services.AppService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import java.util.List;

@Controller
@RequestMapping
@AllArgsConstructor
public class UserWebController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TextRepository textRepository;
    private AppService service;

    @GetMapping("/home")
    @ResponseBody
    public String welcome(){
        return "Welcome to the unprotected page";
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String getUserPage(@PathVariable("id") int id, Model model, @AuthenticationPrincipal User currentUser) {
        Optional<User> viewedUser = userRepository.findById(id);
        // Проверка, что текущий пользователь может просматривать запрашиваемую страницу
        if (currentUser.getId() != id) {
            throw new AccessDeniedException("You are not allowed to view this page.");
        }

        // Загрузка текстов по ID пользователя
        List<Text> texts = textRepository.findByUserId(id);

        // Добавление данных в модель
        model.addAttribute("user", currentUser);
        model.addAttribute("texts", texts);

        return "user_page";
    }
}