package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.*;
import com.OlegKulikov.pastbinclone.try_1.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/texts")
public class TextController {

    @Autowired
    private TextRepository textRepository;

    @PostMapping("/create")
    public Text createText(@RequestBody Text text) {
        text.setCreatedTime(LocalDateTime.now());
        return textRepository.save(text);
    }

    @GetMapping("/user/{id}")
    public List<Text> getTextsByUserId(@PathVariable int id) {
        return textRepository.findByUserId(id);
    }
}
