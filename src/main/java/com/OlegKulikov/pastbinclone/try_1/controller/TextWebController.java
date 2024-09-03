package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.*;
import com.OlegKulikov.pastbinclone.try_1.model.*;
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

    @Autowired
    private CommentRepository commentRepository;

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
    @GetMapping("/edit/{textId}")
    public String showEditTextForm(@PathVariable("textId") int textId, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        Text text = textRepository.findById(textId).orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));

        if (currentUser != null && text.getUser().getId() == currentUser.getId()) {
            model.addAttribute("text", text);
            return "edit_text"; // имя HTML шаблона для редактирования текста
        } else {
            model.addAttribute("errorMessage", "You are not authorized to edit this text.");
            return "redirect:/texts/" + textId;
        }
    }

    @PostMapping("/edit/{textId}")
    public String editText(@PathVariable("textId") int textId, @ModelAttribute Text updatedText, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        Text text = textRepository.findById(textId).orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));

        if (currentUser != null && text.getUser().getId() == currentUser.getId()) {
            text.setTitle(updatedText.getTitle());
            text.setContent(updatedText.getContent());
            textRepository.save(text);
            return "redirect:/texts/" + textId;
        } else {
            model.addAttribute("errorMessage", "You are not authorized to edit this text.");
            return "redirect:/texts/" + textId;
        }
    }

    @PostMapping("/delete/{textId}")
    public String deleteText(@PathVariable("textId") int textId, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        Text text = textRepository.findById(textId).orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));

        if (currentUser != null && text.getUser().getId() == currentUser.getId()) {
            textRepository.delete(text);
            return "redirect:/texts";
        } else {
            model.addAttribute("errorMessage", "You are not authorized to delete this text.");
            return "redirect:/texts/" + textId;
        }
    }

    @PostMapping("/{textId}/comment")
    public String addComment(@PathVariable("textId") int textId, @ModelAttribute UserComment userComment, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser != null) {
            Text text = textRepository.findById(textId).orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));
            userComment.setText(text);
            userComment.setUser(currentUser);
            userComment.setCreatedTime(LocalDateTime.now());
            commentRepository.save(userComment);
            return "redirect:/texts/" + textId;
        } else {
            model.addAttribute("errorMessage", "You need to be logged in to comment.");
            return "redirect:/login";
        }
    }
}