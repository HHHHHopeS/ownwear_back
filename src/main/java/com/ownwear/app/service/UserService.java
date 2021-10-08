package com.ownwear.app.service;


import com.ownwear.app.controller.VisionController;
import com.ownwear.app.exception.ResourceNotFoundException;
import com.ownwear.app.model.Alert;
import com.ownwear.app.model.CurrentUsers;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.AlertRepository;
import com.ownwear.app.repository.CurrentUsersRepository;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.security.UserPrincipal;
import net.sf.json.JSONObject;
import org.joda.time.LocalDate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ownwear.app.form.UserInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CurrentUsersRepository currentUsersRepository;

    @Autowired
    private AlertRepository alertRepository;

    public UserInfo getUserDetail(String username) {

        Optional<User> byusername = userRepository.findByUsername(username);

        if (byusername.isPresent()) {
            UserInfo userInfo = modelMapper.map(byusername.get(), UserInfo.class);
            return userInfo;
        }

        return null;//id로 해당 유저 찾을수 없음
    }

    public List<Post> getUserPosts(String username) {

        Optional<User> byUsername = userRepository.findByUsername(username);

        List<Post> allByUsername = postRepository.findAllByUser(byUsername.get());

        if (allByUsername.isEmpty()) {
            return null;
        }

        return allByUsername;
    }

    public UserInfo getCurrentUser(UserPrincipal userPrincipal, HttpServletRequest request) {

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        //System.out.println(user);
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
        } else {
            //System.out.println("##최초접속 user_id : " + user.getUser_id());
            CurrentUsers currentUsers = new CurrentUsers(user, requestToken);
            currentUsersRepository.save(currentUsers);
            return checkAndReturn(user);
        }
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

    public boolean validationCheck(String value) { //중복값 체크
        boolean contains = value.contains("@");
        if (contains) { //이메일
            Optional<User> byEmail = userRepository.findByEmail(value);
            if (byEmail.isPresent()) {
                return false;
            } else return true;
        } else { //username
            Optional<User> byUsername = userRepository.findByUsername(value);
            if (byUsername.isPresent()) {
                return false;
            } else return true;
        }
    }

    public boolean addInfoOauth2(UserInfo userInfo) {

        try {
            Optional<User> byId = userRepository.findById((userInfo.getUser_id()));
            if (byId.isPresent()) {
                User user = byId.get();
                user.setUsername(userInfo.getUsername());
                user.setEmail(userInfo.getEmail());
                user.setHeight(userInfo.getHeight());
                user.setSex(userInfo.getSex());
                user.setRdate(new Timestamp(System.currentTimeMillis()));
                JSONObject jsonObject = VisionController.uploadImage(userInfo.getUserimg());
                String data = (String)jsonObject.get("data");
                user.setUserimg(data);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}