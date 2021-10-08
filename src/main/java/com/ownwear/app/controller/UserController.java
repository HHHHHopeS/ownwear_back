package com.ownwear.app.controller;

import com.ownwear.app.form.UserInfo;
import com.ownwear.app.exception.ResourceNotFoundException;
import com.ownwear.app.form.UserInfo;
import com.ownwear.app.model.Alert;
import com.ownwear.app.model.CurrentUsers;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.AlertRepository;
import com.ownwear.app.repository.CurrentUsersRepository;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.security.CurrentUser;
import com.ownwear.app.security.UserPrincipal;
import com.ownwear.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/me")
    public UserInfo getCurrentUser(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request) {
         return userService.getCurrentUser(userPrincipal,request);

    }
//    ", user/{username}" permit all
    @GetMapping("/{username}")
    public UserInfo getUserDetail(@PathVariable("username") String username) {

        UserInfo userDetail = userService.getUserDetail(username);

        return userDetail;
    }

    @GetMapping("/{username}/posts")
    public List<Post> getUserPosts(@PathVariable("username") String username) {
        return userService.getUserPosts(username);
    }

    @PostMapping("update/oauth2")
    public boolean addInfoOauth2(@RequestBody UserInfo userInfo){
        return userService.addInfoOauth2(userInfo);

    }
    @PostMapping("update/validationCheck")
    public boolean validationCheck(@RequestBody String value){
        return userService.validationCheck(value);
    }
}
