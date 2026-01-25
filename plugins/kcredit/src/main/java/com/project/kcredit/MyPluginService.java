package com.project.kcredit;

import org.springframework.stereotype.Service;

@Service
public class MyPluginService {

    public String checkCredit() {
        return "Credit score: 850";
    }
}
