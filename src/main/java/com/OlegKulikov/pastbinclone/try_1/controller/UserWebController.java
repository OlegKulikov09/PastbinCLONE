package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.*;
import com.OlegKulikov.pastbinclone.try_1.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Optional;

import java.util.List;

    @Controller
    public class UserWebController {
        @Autowired
        private UserRepository userRepository;
        @Autowired
        private TextRepository textRepository;

        @GetMapping("/users")
        public String getAllUsers(Model model) {
            List<User> users = userRepository.findAll(); // Получаем всех пользователей из базы данных
            model.addAttribute("users", users); // Добавляем пользователей в модель
            return "users"; // Возвращаем имя HTML-шаблона
        }
        @GetMapping("/user/{id}")
        public String getUserPage(@PathVariable("id") int id, Model model, HttpSession session) {
            User currentUser = (User) session.getAttribute("currentUser"); // Получаем текущего пользователя из сессии

            if (currentUser == null || currentUser.getId() != id) {
                return "redirect:/login";  // Если пользователь не залогинен или пытается получить доступ к чужой странице
            }
            List<Text> texts = textRepository.findByUserId(id);
            model.addAttribute("user", currentUser);  // Добавляем пользователя в модель
            model.addAttribute("texts", texts);
            return "user_page";  // Возвращаем шаблон для отображения страницы пользователя
        }
        @GetMapping("/logout")
        public String logout(HttpSession session) {
            // Удаление текущего пользователя из сессии
            session.invalidate();
            // Перенаправление на страницу логина
            return "redirect:/login";
        }
}