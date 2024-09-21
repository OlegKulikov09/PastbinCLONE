package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.*;
import com.OlegKulikov.pastbinclone.try_1.model.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/texts")
public class TextWebController {
    @Autowired
    private TextRepository textRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User getCurrentUser(@AuthenticationPrincipal UserDetails currentUser) {
        return userRepository.findByLogin(currentUser.getUsername());
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String showCreateTextForm(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        model.addAttribute("text", new Text());
        model.addAttribute("currentUser", getCurrentUser(currentUser));
        return "user_new_text"; // имя HTML шаблона для создания текста
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String createText(@ModelAttribute Text text, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = getCurrentUser(currentUser);
        text.setUser(user);
        text.setCreatedTime(LocalDateTime.now());
        Text savedText = textRepository.save(text);
        return "redirect:/texts/" + savedText.getTextId();
    }

    @GetMapping("/{textId}")
    public String viewText(@PathVariable("textId") int textId, Model model) {
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        model.addAttribute("text", text);
        model.addAttribute("formattedDate", text.getCreatedTime().format(formatter));
        List<UserComment> comments = commentRepository.findByText(text);
        model.addAttribute("comments", comments);
        return "view_text"; // имя HTML шаблона для просмотра текста
    }

    @GetMapping("/edit/{textId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String showEditTextForm(@PathVariable("textId") int textId, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = getCurrentUser(currentUser);
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));

        if (text.getUser().getId() == user.getId()) {
            model.addAttribute("text", text);
            return "edit_text"; // имя HTML шаблона для редактирования текста
        } else {
            model.addAttribute("errorMessage", "You are not authorized to edit this text.");
            return "redirect:/texts/" + textId;
        }
    }

    @PostMapping("/edit/{textId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String editText(@PathVariable("textId") int textId, @ModelAttribute Text updatedText, @AuthenticationPrincipal UserDetails currentUser) {
        User user = getCurrentUser(currentUser);
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));

        if (text.getUser().getId() == user.getId()) {
            text.setContent(updatedText.getContent());
            textRepository.save(text);
            return "redirect:/texts/" + textId;
        } else {
            return "You are not authorized to edit this text.";
        }
    }
//ИСПРАВИТЬ ЛОМАЕТ ПРОСМОТР БЕЗ РОЛИ
    @PostMapping("/{textId}/comment")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String addComment(@PathVariable("textId") int textId, @ModelAttribute UserComment userComment, @AuthenticationPrincipal UserDetails currentUser) {
        User user = getCurrentUser(currentUser);
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));
        userComment.setText(text);
        userComment.setUser(user);
        userComment.setCreatedTime(LocalDateTime.now());
        commentRepository.save(userComment);
        return "redirect:/texts/" + textId;
    }

    @PostMapping("/delete/{textId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String deleteText(@PathVariable("textId") int textId, @ModelAttribute @AuthenticationPrincipal UserDetails currentUser) {
        User user = getCurrentUser(currentUser);
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));

        if (text.getUser().getId() == user.getId()) {
            textRepository.delete(text);
            return "redirect:/texts";
        } else {
            return "You are not authorized to delete this text.";
        }
    }
}