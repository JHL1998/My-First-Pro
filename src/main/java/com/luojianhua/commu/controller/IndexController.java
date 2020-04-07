package com.luojianhua.commu.controller;

import com.luojianhua.commu.dto.PageDTO;
import com.luojianhua.commu.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name="page",defaultValue = "1") Integer page,
                        @RequestParam(name="size",defaultValue = "5") Integer size,
                        @RequestParam(name="search",required = false)  String search) {


        PageDTO pageDTO=questionService.find(search,page,size);
        model.addAttribute("pagination",pageDTO);
        model.addAttribute("search",search);


        return "index";

    }
}
