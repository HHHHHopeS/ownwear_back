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
    private UserRepository userRepository;

    @Autowired
    private CurrentUsersRepository currentUsersRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AlertRepository alertRepository;

    final ModelMapper modelMapper = new ModelMapper();
    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserInfo getCurrentUser(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        System.out.println(user);
        Optional<CurrentUsers> byUser = currentUsersRepository.findByUser(user);
        String requestToken = request.getHeader("Authorization");
        if (requestToken != null && requestToken.startsWith("Bearer")) {
            requestToken = requestToken.substring(7);
        }
        if (byUser.isPresent()) {
            CurrentUsers currentUsers = byUser.get();
            if (currentUsers.getToken().equals(requestToken)) {

                return checkAndReturn(user);
            }
            return null; //todo 유효하지않은 토큰입니다.
        }else {
            System.out.println("##최초접속 user_id : "+user.getUser_id());
            CurrentUsers currentUsers = new CurrentUsers(user,requestToken);
            currentUsersRepository.save(currentUsers);
            return checkAndReturn(user);
        }

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
    
    private UserInfo checkAndReturn(User user){

        UserInfo userInfo = modelMapper.map(user, UserInfo.class);
        List<Alert> falseByUser = alertRepository.findFalseByUser(user);
        if (falseByUser.isEmpty()){
            userInfo.setIschecked(true);
        }else {
            userInfo.setIschecked(false);
        }
        return userInfo;
    }
}
