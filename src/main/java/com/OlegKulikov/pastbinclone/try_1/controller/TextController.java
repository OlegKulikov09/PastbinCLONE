package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.*;
import com.OlegKulikov.pastbinclone.try_1.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api/texts")
public class TextController {

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private TagRepository tagRepository;

    @PostMapping("/create")
    public Text createText(@RequestBody Text text) {
        text.setCreatedTime(LocalDateTime.now());

        List<Tag> tags = new ArrayList<>();
        for(Tag tag : text.getTags()) {
            Tag existingTag = tagRepository.findByNameOfTag(tag.getNameOfTag());
            if (existingTag == null) {
                tagRepository.save(tag);
                tags.add(tag);
            }
            else tags.add(existingTag); }
            text.setTags(tags);

        return textRepository.save(text);
    }

    @GetMapping("/user/{userId}")
    public List<Text> getTextsByUserId(@PathVariable Long userId) {
        return textRepository.findByUserId(userId);
    }
    @GetMapping("/tag/{tagName}")
    public List<Text> getTextsByTag(@PathVariable String nameOfTag) {
        Tag tag = tagRepository.findByNameOfTag(nameOfTag);
        return tag != null ? tag.getTexts() : new ArrayList<>();
    }
}
