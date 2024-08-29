package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.TextRepository;
import com.OlegKulikov.pastbinclone.try_1.model.Text;
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

    @GetMapping("/create")
    public String showCreateTextForm(Model model) {
        model.addAttribute("text", new Text());
        return "user_new_text"; // имя HTML шаблона для создания текста
    }

    @PostMapping("/create")
    public String createText(@ModelAttribute Text text, Model model) {
        text.setCreatedTime(LocalDateTime.now());
        Text savedText = textRepository.save(text);
        return "redirect:/texts/view/" + savedText.getTextId(); // Перенаправление после создания
    }

    @GetMapping("/view/{textId}")
    public String viewText(@PathVariable("textId") int textId, Model model) {
        Text text = textRepository.findById(textId).orElseThrow(() -> new IllegalArgumentException("Invalid text Id:" + textId));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        model.addAttribute("text", text);
        model.addAttribute("formattedDate", text.getCreatedTime().format(formatter));
        return "view_text"; // имя HTML шаблона для просмотра текста
    }

}