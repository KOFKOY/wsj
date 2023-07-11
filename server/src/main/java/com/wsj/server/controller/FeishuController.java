package com.wsj.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fs")
public class FeishuController {
    @PostMapping
    public Map feishu(@RequestBody Map map) {
        Map result = new HashMap();
        if (map.containsKey("challenge")) {
            result.put("challenge", map.get("challenge"));
            return result;
        }
        return result;
    }
}
