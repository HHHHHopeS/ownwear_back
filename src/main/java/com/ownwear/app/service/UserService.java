package com.ownwear.app.service;


import com.ownwear.app.exception.ResourceNotFoundException;
import com.ownwear.app.form.*;
import com.ownwear.app.model.Alert;
import com.ownwear.app.model.CurrentUsers;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.*;
import com.ownwear.app.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    final ModelMapper modelMapper = new ModelMapper();


    private PasswordEncoder passwordEncoder;


    private UserRepository userRepository;


    private PostRepository postRepository;


    private CurrentUsersRepository currentUsersRepository;


    private AlertRepository alertRepository;
    private CommentRepository commentRepository;
    private LikePostRepository likePostRepository;
    private FollowRepository followRepository;

    public UserInfo getUserDetail(String username, Long current_userid) {

        Optional<User> tagetUserOp = userRepository.findByUsername(username);
        UserInfo targetUserInfo = null;
        Optional<User> currentUserOp = null;
        if (current_userid != null) {
            currentUserOp = userRepository.findById(current_userid);
            if (tagetUserOp.isPresent()) {
                User targetUser = tagetUserOp.get();
                User currentUser = currentUserOp.get();
                targetUserInfo = modelMapper.map(targetUser, UserInfo.class);
                targetUserInfo.setIsfollowing(followRepository.findByUsers(currentUser, targetUser).isPresent());
                return targetUserInfo;
            } else return null;
            //id로 해당 유저 찾을수 없음
        } else { //현재 비회원유저
            if (tagetUserOp.isPresent()) {
                User targetUser = tagetUserOp.get();
                targetUserInfo = modelMapper.map(targetUser, UserInfo.class);
                targetUserInfo.setIsfollowing(false);
                return targetUserInfo;
            } else return null;
            //id로 해당 유저 찾을수 없음
        }
    }


    public Page<IndexPost> getUserPosts(String username,int page) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        PageRequest pageRequest = PageRequest.of(page,12);
        if (byUsername.isPresent()) {
            Page<IndexPost> indexPosts = postRepository.findAllByUser(byUsername.get(),pageRequest)
                    .map(Post -> modelMapper.map(Post,IndexPost.class));

            indexPosts = (Page<IndexPost>) setIndexPosts(indexPosts);

            return indexPosts;
        }
        return null;
    }

    private Iterable<IndexPost> setIndexPosts(Iterable<IndexPost> indexPosts) {

        for (IndexPost indexPost : indexPosts) {
            indexPost.setCommentcount(commentRepository.countAllByPostPostid(indexPost.getPostid()));
            indexPost.setLikecount(likePostRepository.countByPostPostid(indexPost.getPostid()));

        }
        return indexPosts;
    }

    private List<IndexPost> getIndexPosts(List<IIndexPost> iIndexPosts) {
        List<IndexPost> indexPosts = new ArrayList<>();
        for (IIndexPost iIndexPost : iIndexPosts) {
            IndexPost indexPost = new IndexPost();
            indexPost.setPostid(iIndexPost.getPostid());
            indexPost.setImgData(iIndexPost.getImgdata());
            indexPost.setRdate(iIndexPost.getRdate());
            indexPosts.add(indexPost);
        }
        return indexPosts;
    }
    private List<IndexPost> getIndexPosts(Page<Post> posts) {
        List<IndexPost> indexPosts = new ArrayList<>();
        for (Post post : posts) {
            IndexPost indexPost =modelMapper.map(post,IndexPost.class);
            indexPosts.add(indexPost);
        }
        return indexPosts;
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
            //System.out.println("##최초접속 userid : " + user.getUserid());
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
            Optional<User> byId = userRepository.findById((userInfo.getUserid()));
            if (byId.isPresent()) {
                User user = byId.get();
            }
        } catch (Exception e) {

        }
        return false;
    }

    public UserForm getUserData(Long userid) {

        Optional<User> byId = userRepository.findById(userid);

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
        return modelMapper.map(userRepository.save(user), UserForm.class);
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

        Optional<User> byId = userRepository.findById(userpwdForm.getUserid());
        userpwdForm.setPassword(passwordEncoder.encode(userpwdForm.getPassword()));
        User user = modelMapper.map(userpwdForm, User.class);

        return modelMapper.map(userRepository.save(user), UserPwdForm.class);
    }
}
