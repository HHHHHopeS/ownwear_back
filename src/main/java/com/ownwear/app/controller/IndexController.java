package com.ownwear.app.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.config.auth.PrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

//@ResponseBody
@Controller
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrincipalDetailsService userService;

    @GetMapping({"/", ""})
    public String index() {

        //머스테치 기본폴더 src/main/resources/
        //뷰 리졸버 설정 : templates(prefix), .mustache(suffix) applcation.yml에 등록돼잇음 디펜던시 설정시 안해줘도됨
        return "index";
    }

    @GetMapping("/replace")
    public @ResponseBody String replace(HttpServletRequest http) {

        return "<form method='post' action='/replace'><input type='text' name='jwtToken'/> <button>입력</button></form>";
    }

    @PostMapping("/replace")
    public String replace2(String jwtToken) {
        /*
        System.out.println(http.getSession().getAttribute("SPRING_SECURITY_CONTEXT"));
        PrincipalDetails principal = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principal.getUser();
        if (user.getEmail().equals(null) && userInfo.getEmail().equals(null)) {
        } else if (user.getEmail().equals(null) && !userInfo.getEmail().equals(null)) {
            if (!userInfo.getUsername().equals(null)) {
                user.setEmail(userInfo.getEmail());
                user.setVerified(true);
                user.setUsername(userInfo.getUsername());
                userRepository.save(user);
                return user;
            }
        }*/

        /*eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJob3Blc-2GoO2BsCIsInByb3ZpZGVyIjoiZmFjZWJvb2siLCJwcm92aWRlcklkIjoiZmFjZWJvb2tfMjM2NDY5ODMyMzY2NDAxMSIsImlkIjowLCJleHAiOjE2MzE5NTY5MjAsInVzZXJuYW1lIjoiZmFjZWJvb2tfMjM2NDY5ODMyMzY2NDAxMSJ9.PwnqSZxmKYiI0OO4Ut9PgTt7w2Mo0316It4bEgXc2aHKOTmt4bfjKmznJCTxv5i5dfNdCzkZBsFbmnClCF9KYQ*/
        final DecodedJWT hopes = JWT.require(Algorithm.HMAC512("hopes")).build().verify(jwtToken);
        String username = hopes.getClaim("username").asString();
        Set<String> strings = hopes.getClaims().keySet();
        for (String s : strings){
            System.out.println(s);

        }

        return username;
    }

    //@AuthenticationPrincipal PrincipalDetails principalDetails)
    @GetMapping("/user")
    public @ResponseBody User user() {
        User user = new User();
        return user;
    }

    @GetMapping("/admin")
    public User admin() {
        User user = new User();
        return user;
    }

    @GetMapping("/manager")
    public User manager() {
        User user = new User();
        return user;
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
    public User join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        return userService.save(user);
    }


}
