package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.TagRepository;
import com.OlegKulikov.pastbinclone.try_1.Repositories.TextRepository;
import com.OlegKulikov.pastbinclone.try_1.model.Tag;
import com.OlegKulikov.pastbinclone.try_1.model.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/texts")
public class TextWebController {
    @Autowired
    private TextRepository textRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/create")
    public String showCreateTextForm(Model model) {
        model.addAttribute("text", new Text());
        return "user_new_text"; // имя HTML шаблона для создания текста
    }

    @PostMapping("/create")
    public String createText(@ModelAttribute Text text, @RequestParam String tags, Model model) {
        text.setCreatedTime(LocalDateTime.now());

        // Разделение тегов и присвоение тексту
        List<Tag> tagList = Arrays.stream(tags.split(","))
                .map(String::trim)
                .map(nameOfTag -> {
                    Tag existingTag = tagRepository.findByNameOfTag(nameOfTag);
                    if (existingTag == null) {
                        Tag newTag = new Tag();
                        newTag.setNameOfTag(nameOfTag);
                        tagRepository.save(newTag);
                        return newTag;
                    }
                    return existingTag;
                })
                .collect(Collectors.toList());

        text.setTags(tagList);
        textRepository.save(text);

        return "redirect:/texts/create"; // Перенаправление после создания
    }
}