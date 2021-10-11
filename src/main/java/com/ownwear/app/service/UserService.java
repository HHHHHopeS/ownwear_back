package com.ownwear.app.service;


import com.ownwear.app.exception.ResourceNotFoundException;
import com.ownwear.app.form.UserPwdForm;
import com.ownwear.app.model.Alert;
import com.ownwear.app.model.CurrentUsers;
import com.ownwear.app.form.UserForm;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.AlertRepository;
import com.ownwear.app.repository.CurrentUsersRepository;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.security.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ownwear.app.form.UserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            }
        } catch (Exception e) {

        }
        return false;
    }

    public UserForm getUserData(Long user_id) {

        Optional<User> byId = userRepository.findById(user_id);

        if (byId.isPresent()) {
            UserForm map = modelMapper.map(byId.get(), UserForm.class);
            return map;
        }

        return null;

    }

    public UserForm updateUser(UserInfo userInfo) {

        Optional<User> byUsername = userRepository.findByUsername(userInfo.getUsername());
        Optional<User> byEmail = userRepository.findByEmail(userInfo.getEmail());

        if (byUsername.isPresent()) {

            return null;
        } else if (byEmail.isPresent()) {
            return null;
        }

//                user.setUserimg(userInfo.getUserimg());
//                user.setUsername(userInfo.getUsername());
//                user.setEmail(userInfo.getEmail());
//                user.setHeight(userInfo.getHeight());
//                user.setSex(userInfo.getSex());
//                user.setRdate(new Timestamp(System.currentTimeMillis()));
//                JSONObject jsonObject = VisionController.uploadImage(userInfo.getUserimg());
//                String data = (String) jsonObject.get("data");
//                user.setUserimg(data);
        User user = new User();
            return modelMapper.map(userRepository.save(user),UserForm.class);
        }

    public boolean checkPw(String pw, Long id) {

        Optional<User> byId = userRepository.findById(id);

        String password = byId.get().getPassword();

        String newPassword = passwordEncoder.encode(pw);

        if (newPassword.equals(password)) {
            return true;
        }

        return false;
    }

    public UserPwdForm updatePw(UserPwdForm userpwdForm) {

        Optional<User> byId = userRepository.findById(userpwdForm.getUser_id());
        userpwdForm.setPassword(passwordEncoder.encode(userpwdForm.getPassword()));
        User user = modelMapper.map(userpwdForm, User.class);

        return modelMapper.map(userRepository.save(user), UserPwdForm.class);
    }
}
