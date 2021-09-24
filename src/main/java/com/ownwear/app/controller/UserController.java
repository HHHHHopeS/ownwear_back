package com.ownwear.app.controller;

import com.ownwear.app.exception.ResourceNotFoundException;
import com.ownwear.app.model.CurrentUsers;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.CurrentUsersRepository;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.security.CurrentUser;
import com.ownwear.app.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUsersRepository currentUsersRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request) {
        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        Optional<CurrentUsers> byId = currentUsersRepository.findById(user.getId());
        if (byId.isPresent()) {
            CurrentUsers currentUsers = byId.get();
            System.out.println("user/me currnet token: " + currentUsers.getToken());
            String requestToken = request.getHeader("Authorization");
            if (requestToken != null && requestToken.startsWith("Bearer")) {
                requestToken = requestToken.substring(7);
            }
            System.out.println("request token: " + requestToken);
            if (currentUsers.getToken().equals(requestToken)) {
                System.out.println("일치");
                return user;
            }
            return null;
        }
        return null;
    }
}
