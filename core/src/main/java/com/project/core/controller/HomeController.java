package com.project.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to Core Application!");
        model.addAttribute("description", "This is the main application");
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About Core");
        model.addAttribute("content", "This is the core module of the Spring Template application");
        return "about";
    }
}