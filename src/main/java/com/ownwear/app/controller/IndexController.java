package com.ownwear.app.controller;

import com.ownwear.app.form.SearchForm;
import com.ownwear.app.model.User;
import com.ownwear.app.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    IndexService indexService;

    @GetMapping("/srchdata")
    public SearchForm SrchData(String username) {

        SearchForm searchForm = indexService.SrchUserData(username);

        return searchForm;
    }

}
