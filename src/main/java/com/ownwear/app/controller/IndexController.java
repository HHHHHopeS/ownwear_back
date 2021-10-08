package com.ownwear.app.controller;

import com.ownwear.app.model.User;
import com.ownwear.app.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("index")
public class IndexController {

    @Autowired
    IndexService indexService;


    @GetMapping("/srchdata")
    public List<User> SrchData(String username) {

        List<User> users = indexService.SrchUserData(username);

        return users;
    }

}
