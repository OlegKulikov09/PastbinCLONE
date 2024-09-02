package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.*;
import com.OlegKulikov.pastbinclone.try_1.model.Text;
import com.OlegKulikov.pastbinclone.try_1.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/texts")
public class TextWebController {
    @Autowired
    private TextRepository textRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/create")
    public String showCreateTextForm(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            model.addAttribute("text", new Text());
            return "user_new_text"; // имя HTML шаблона для создания текста
        } else {
            model.addAttribute("errorMessage", "You need to be logged in to create a text.");
            return "redirect:/login"; // Перенаправление на страницу логина, если пользователь не залогинен
        }
    }

    @PostMapping("/create")
    public String createText(@ModelAttribute Text text, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            text.setUser(currentUser);
            text.setCreatedTime(LocalDateTime.now());
            Text savedText = textRepository.save(text);
            model.addAttribute("textId", savedText.getTextId());
            return "redirect:/texts/" + savedText.getTextId();
        } else {
            model.addAttribute("errorMessage", "You need to be logged in to create a text.");
            return "redirect:/login";
        }
    }

    @GetMapping("{textId}")
    public String viewText(@PathVariable("textId") int textId, Model model) {
        Text text = textRepository.findById(textId).orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        model.addAttribute("text", text);
        model.addAttribute("formattedDate", text.getCreatedTime().format(formatter));
        return "view_text"; // имя HTML шаблона для просмотра текста
    }
}