package com.project.kcredit;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MyPluginController {


    @GetMapping("/main.do")
    public String home(Model model) {
        model.addAttribute("pluginName", "Kcredit Plugin");
        model.addAttribute("description", "Credit checking and validation plugin");
        model.addAttribute("version", "0.0.1");
        return "main";
    }
}
