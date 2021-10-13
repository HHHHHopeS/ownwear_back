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

    @Autowired //todo AllArgsConstructor
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
    @GetMapping("/{username}") //todo 팔로잉 했는지,
    public UserInfo getUserDetail(@PathVariable("username") String username) {

        UserInfo userDetail = userService.getUserDetail(username);

        return userDetail;
    }
//todo modal (좋,팔 형식) , (타입(좋,팔), 현재 유저, 타겟 유저(포스트))
    //    ", /user/{username}/posts" permit all
    @GetMapping("/{username}/posts") //todo 인피니티 스크롤,포스트 12 개를 유저 정보와 함께
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
    public UserForm getUserData(@RequestBody Long userid) {

//        System.out.println("userid = " + userid);

        UserForm userData = userService.getUserData(userid);

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
        System.out.println("pw = " + pw);
        long id = userpwdForm.getUserid();
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
