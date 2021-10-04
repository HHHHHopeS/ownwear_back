package com.ownwear.app.controller;

import com.ownwear.app.model.LikePost;
import com.ownwear.app.repository.LikePostRepository;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.service.LikePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/like")
public class LikePostController {

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikePostService likePostService;

    //,"like/getlist/**" permit all
    @PostMapping("/getlist")
    public List<LikePost> getLikeUser(@RequestBody LikePost likePost) {
//        System.out.println(likePost.getPost().getPost_id());
        List<LikePost> byPost = likePostRepository.findByPost(likePost.getPost());

        return byPost;
    }

    //     ",like/checklike/**" authenticate
    @PostMapping("/checklike")
    public List<LikePost> checklike(@RequestBody LikePost likePost) {

        List<LikePost> likePosts = likePostRepository.findByUser(likePost.getUser());

        return likePosts;
    }

    //    ",like/check/**" authenticate
    @PostMapping("/toggle")
    public boolean likeToggle(LikePost likePost) {
        return likePostService.likeToggle(likePost);
    }

//    ",like/likecount/**" permit all
    @PostMapping("/likecount")
    public long likecount(LikePost likePost) {
        return likePostService.likecount(likePost);
    }
}
// 10-04 merge and push
