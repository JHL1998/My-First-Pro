package com.luojianhua.commu.controller;

import com.luojianhua.commu.cache.HotTagCache;
import com.luojianhua.commu.dto.PageDTO;
import com.luojianhua.commu.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private HotTagCache hotTagCache;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag) {

        PageDTO pageDTO = questionService.find(tag, search, page, size);
        List<String> tags = hotTagCache.getHot();
        model.addAttribute("pagination", pageDTO);
        model.addAttribute("search", search);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", tags);

        return "index";

    }
}
