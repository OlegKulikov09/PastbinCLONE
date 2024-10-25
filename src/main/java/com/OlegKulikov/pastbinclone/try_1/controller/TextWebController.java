package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.*;
import com.OlegKulikov.pastbinclone.try_1.model.*;
import com.OlegKulikov.pastbinclone.try_1.services.RatingService;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/texts")
public class TextWebController {
    @Autowired
    private TextRepository textRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RatingService ratingService;

    private User getCurrentUser(@AuthenticationPrincipal UserDetails currentUser) {
        return userRepository.findByLogin(currentUser.getUsername());
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String showCreateTextForm(Model model, @AuthenticationPrincipal UserDetails currentUser) {
        model.addAttribute("text", new Text());
        model.addAttribute("currentUser", getCurrentUser(currentUser));
        return "user_new_text";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String createText(@ModelAttribute Text text, Model model,
                             @AuthenticationPrincipal UserDetails currentUser) {
        User user = getCurrentUser(currentUser);
        text.setUser(user);
        text.setCreatedTime(LocalDateTime.now());
        Text savedText = textRepository.save(text);
        return "redirect:/texts/" + savedText.getTextId();
    }

    @GetMapping("/{textId}")
    public String viewText(@PathVariable("textId") String textId, Model model,
                           @AuthenticationPrincipal UserDetails currentUser) {
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));
        if (currentUser != null) {
            User user = getCurrentUser(currentUser);
            model.addAttribute("user", user);
            model.addAttribute("currentUsername", currentUser.getUsername());
        }
        model.addAttribute("text", text);
        List<UserComment> comments = commentRepository.findByText(text);
        model.addAttribute("comments", comments);
        return "view_text";
    }

    @PutMapping("/edit/{textId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String editText(@PathVariable("textId") String textId,
                           @RequestParam("title") String title,
                           @RequestParam("content") String content,
                           @AuthenticationPrincipal UserDetails currentUser) {
        User user = getCurrentUser(currentUser);
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));
        if (text.getUser().getId() == user.getId()) {
            text.setTitle(title);
            text.setContent(content);
            textRepository.save(text);
            return "redirect:/texts/" + textId;
        } else {
            return "You are not authorized to edit this text.";
        }
    }

    @PostMapping("/{textId}/comment")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String addComment(@PathVariable("textId") String textId,
                             @ModelAttribute UserComment userComment,
                             @AuthenticationPrincipal UserDetails currentUser) {
        User user = getCurrentUser(currentUser);
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));
        userComment.setText(text);
        userComment.setUser(user);
        userComment.setCreatedTime(LocalDateTime.now());
        commentRepository.save(userComment);
        return "redirect:/texts/" + textId;
    }

    @DeleteMapping("/delete/{textId}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String deleteText(@PathVariable("textId") String textId,
                             @AuthenticationPrincipal UserDetails currentUser) {
        User user = getCurrentUser(currentUser);
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));

        if (text.getUser().getId() == user.getId()) {
            textRepository.delete(text);
            return "redirect:/user/" + user.getId();
        } else {
            return "You are not authorized to delete this text.";
        }
    }

    @PostMapping("/{textId}/rate")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String rateText(@PathVariable String textId,
                           @AuthenticationPrincipal UserDetails currentUser) {
        User user = getCurrentUser(currentUser);
        ratingService.changeRating(textId, user);
        return "redirect:/texts/" + textId;
    }

    @GetMapping("/rating_all")
    public String showRatingAllTexts(Model model) {
        List<Text> topRated10Texts = textRepository.orderByRate10Desc();
        model.addAttribute("topRated10Texts", topRated10Texts);
        return "rating_all";
    }
}