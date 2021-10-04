package com.ownwear.app.controller;

import com.ownwear.app.exception.ResourceNotFoundException;
import com.ownwear.app.form.UserInfo;
import com.ownwear.app.model.CurrentUsers;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.CurrentUsersRepository;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.security.CurrentUser;
import com.ownwear.app.security.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUsersRepository currentUsersRepository;

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

                return modelMapper.map(user, UserInfo.class);
            }
            return null;
        }else {
            System.out.println("##최초접속 user_id : "+user.getUser_id());
            CurrentUsers currentUsers = new CurrentUsers(user,requestToken);
            currentUsersRepository.save(currentUsers);
            return modelMapper.map(user, UserInfo.class);
        }

    }
}
