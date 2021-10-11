package com.ownwear.app.controller;

import com.ownwear.app.form.UserForm;
import com.ownwear.app.form.UserInfo;
import com.ownwear.app.form.UserPwdForm;
import com.ownwear.app.model.Alert;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.AlertRepository;
import com.ownwear.app.security.CurrentUser;
import com.ownwear.app.security.UserPrincipal;
import com.ownwear.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private AlertRepository alertRepository;

    final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/me")
    public UserInfo getCurrentUser(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request) {
        return userService.getCurrentUser(userPrincipal, request);

    }

    //    ", user/{username}" permit all
    @GetMapping("/{username}")
    public UserInfo getUserDetail(@PathVariable("username") String username) {

        UserInfo userDetail = userService.getUserDetail(username);

        return userDetail;
    }

    //    ", /user/{username}/posts" permit all
    @GetMapping("/{username}/posts")
    public List<Post> getUserPosts(@PathVariable("username") String username) {
        return userService.getUserPosts(username);
    }

    @PostMapping("update/oauth2")
    public boolean addInfoOauth2(@RequestBody UserInfo userInfo) {
        return userService.addInfoOauth2(userInfo);
    }

    @PostMapping("update/validationCheck")
    public boolean validationCheck(@RequestBody String value) {
        return userService.validationCheck(value);
    }

    //    ", /user/mypage/**" auth
    @PostMapping("/mypage")
    public UserForm getUserData(@RequestBody Long user_id) {

//        System.out.println("user_id = " + user_id);

        UserForm userData = userService.getUserData(user_id);

        return userData;
    }

    @PostMapping("/mypage/update")
    public UserForm UpdateUser(UserInfo userInfo) {

        UserForm userForm = userService.updateUser(userInfo);

        return userForm;
    }
//",/user/updateprofile" auth
    @PostMapping("/updateprofile")
    public UserPwdForm updateprofile(@RequestBody UserPwdForm userpwdForm) {
        UserPwdForm updatepf = userService.updatePw(userpwdForm);

        return updatepf;
    }
//",/user/checkpw" auth
    @PostMapping("/checkpw")
    public boolean checkPw(@RequestBody UserPwdForm userpwdForm) {
        String pw = userpwdForm.getPassword();
        long id = userpwdForm.getUser_id();
        return userService.checkPw(pw, id);
    }

    private UserInfo checkAndReturn(User user) {

        UserInfo userInfo = modelMapper.map(user, UserInfo.class);

        List<Alert> falseByUser = alertRepository.findFalseByUser(user);

        if (falseByUser.isEmpty()) {
            userInfo.setIschecked(true);
        } else {
            userInfo.setIschecked(false);
        }

        return userInfo;
    }
}
