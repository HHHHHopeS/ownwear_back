package com.ownwear.app.controller;

import com.ownwear.app.form.UserInfo;
import com.ownwear.app.exception.ResourceNotFoundException;
import com.ownwear.app.model.CurrentUsers;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.CurrentUsersRepository;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.security.CurrentUser;
import com.ownwear.app.security.UserPrincipal;
import com.ownwear.app.service.UserService;
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

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request) {
        System.out.println("##userme user: " + userPrincipal);
        System.out.println("##userme getId: " + userPrincipal.getId());
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
            System.out.println("##user/me current token: " + currentUsers.getToken());
            System.out.println("##request token: " + requestToken);
            if (currentUsers.getToken().equals(requestToken)) {
                System.out.println("일치");
                return user;
            }
            return null;
        } else {
            System.out.println("##최초접속 user_id : " + user.getUser_id());
            CurrentUsers currentUsers = new CurrentUsers(user, requestToken);
            currentUsersRepository.save(currentUsers);
        }

        return null;
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
}
