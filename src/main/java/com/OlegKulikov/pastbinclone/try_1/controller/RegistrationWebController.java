package com.OlegKulikov.pastbinclone.try_1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationWebController {

    @GetMapping("/user_creating_form")
    public String showUserCreatingForm() {
        return "user_creating_form"; // Это имя вашего шаблона без .html
    }
}
