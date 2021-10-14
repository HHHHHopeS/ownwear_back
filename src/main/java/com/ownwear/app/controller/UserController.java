package com.ownwear.app.controller;

import com.ownwear.app.dto.*;
import com.ownwear.app.entity.Alert;
import com.ownwear.app.entity.User;
import com.ownwear.app.repository.AlertRepository;
import com.ownwear.app.security.CurrentUser;
import com.ownwear.app.security.UserPrincipal;
import com.ownwear.app.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private AlertRepository alertRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/me")
    public UserInfo getCurrentUser(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request) {
        return userService.getCurrentUser(userPrincipal, request);

    }

    @GetMapping("/{username}") //todo 팔로우 유무 팔로우 팔로워 수
    public UserProfile getUserDetail(@PathVariable("username") String username, Long current_userid) {

        UserProfile userDetail = userService.getUserDetail(username, current_userid);

        return userDetail;
    }

    //modal (타입(좋,팔), 현재 유저, 타겟 유저(포스트))
    @PostMapping("modal")
    public List<ListModalForm> getModalData(@RequestBody ListModalRequest listModalRequest) {
        return userService.getListModal(listModalRequest);
    }

    @GetMapping("/{username}/posts") //인피니티 스크롤,포스트 12 개를 유저 정보와 함께
    public Page<IndexPost> getUserPosts(@PathVariable("username") String username) {
        Page<IndexPost> userPosts = userService.getUserPosts(username, 0);
        return userPosts;
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
    public UserForm UpdateUser(@RequestBody UserInfo userInfo) {

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
