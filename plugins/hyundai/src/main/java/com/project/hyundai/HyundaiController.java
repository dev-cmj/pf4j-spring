package com.project.hyundai;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/main")
public class HyundaiController {

    @GetMapping("/main.do")
    public String home(Model model) {
        model.addAttribute("pluginName", "Hyundai Credit Verification");
        model.addAttribute("version", "1.0.0");
        model.addAttribute("description", "현대차 신용 인증 플러그인. REST API 연동 및 실시간 점검 제공.");
        model.addAttribute("pluginType", "Credit Verification");
        model.addAttribute("provider", "Hyundai Motor VDI Team");
        return "main";
    }
}
