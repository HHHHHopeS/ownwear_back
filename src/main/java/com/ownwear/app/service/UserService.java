package com.ownwear.app.service;


import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ownwear.app.form.UserInfo;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    public UserInfo getUserDetail(String username) {

        Optional<User> byusername = userRepository.findByUsername(username);

        if(byusername.isPresent()){
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
}