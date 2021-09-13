package com.ownwear.app.controller;

import com.ownwear.app.model.User;
import com.ownwear.app.service.PrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//@ResponseBody
@Controller
public class IndexController {

    @Autowired
    private PrincipalService userService;

    @GetMapping({"/", ""})
    public String index() {
        //머스테치 기본폴더 src/main/resources/
        //뷰 리졸버 설정 : templates(prefix), .mustache(suffix) applcation.yml에 등록돼잇음 디펜던시 설정시 안해줘도됨
        return "index";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/loginform")
    public String loginform() {
        return "loginform";
    }

    @GetMapping("/joinform")
    public String joinform() {
        return "joinform";
    }

    @PostMapping("/join")
    public @ResponseBody User join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        return userService.save(user);

    }


}
