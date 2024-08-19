package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.UserRepository;
import com.OlegKulikov.pastbinclone.try_1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserWebController {
@Autowired
private UserRepository userRepository;

@GetMapping("/users")
public String getAllUsers(Model model) {
    List<User> users = userRepository.findAll(); // Получаем всех пользователей из базы данных
    model.addAttribute("users", users); // Добавляем пользователей в модель
    return "users"; // Возвращаем имя HTML-шаблона
}
}