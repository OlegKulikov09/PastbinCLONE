package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.*;
import com.OlegKulikov.pastbinclone.try_1.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/texts")
public class TagWebController {

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/tag")
    public String searchByTag(@RequestParam String tagName, Model model) {
        Tag tag = tagRepository.findByNameOfTag(tagName);
        List<Text> texts = tag != null ? tag.getTexts() : new ArrayList<>();
        model.addAttribute("texts", texts);
        return "search_text_tags"; // имя HTML шаблона для поиска по тегам
    }
}
