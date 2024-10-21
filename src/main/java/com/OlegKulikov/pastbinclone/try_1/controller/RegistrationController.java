package com.OlegKulikov.pastbinclone.try_1.controller;

import com.OlegKulikov.pastbinclone.try_1.model.User;
import com.OlegKulikov.pastbinclone.try_1.services.MyUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegistrationController {
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm")
                              @Valid User userForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!myUserDetailsService.saveUser(userForm)){
            model.addAttribute("usernameError", "Login already exists");
            return "registration";
        }
        return "redirect:/login";
    }
}