package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.Repositories.TextRepository;
import com.OlegKulikov.pastbinclone.try_1.model.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/texts")
public class TextController {

    @Autowired
    private TextRepository textRepository;

    @PostMapping("/create")
    public Text createText(@RequestBody Text text) {
        text.setCreatedTime(LocalDateTime.now());
        return textRepository.save(text);
    }

    @GetMapping("/user/{userId}")
    public List<Text> getTextsByUserId(@PathVariable Long userId) {
        return textRepository.findByUserId(userId);
    }
}
