package com.ownwear.app.controller;

import com.ownwear.app.model.HashTag;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.vo.PostVo;
import com.ownwear.app.model.Comment;
import com.ownwear.app.model.Post;
import com.ownwear.app.repository.CommentRepository;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.vo.UserRelatedVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/detail")
@Slf4j
public class DetailController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{post_id}")
    public PostVo getDetail(@PathVariable("post_id") Long post_id){
        log.info("들어온값 {}", post_id);
        Optional<Post> byPostno = postRepository.findById(post_id);
        if (byPostno.isPresent()){
            Post post = byPostno.get();
            System.out.println(userRepository.findById(post.getPost_id()));

            String username = userRepository.findById(post.getPost_id()).get().getUsername();
            int likecount = 1;
            ArrayList<HashTag> hashtags = null;
            ArrayList<UserRelatedVo> userRelated = null;
            ArrayList<Comment> comments = commentRepository.findByPost(post);

            PostVo postVo = new PostVo(post,likecount,hashtags,userRelated,comments,username);
            return postVo;
        }
        return null;
    }

    @PostMapping("/create")
    public Post createPost(@RequestBody Post post){
        Optional<Post> byId = postRepository.findById(post.getPost_id());
        if (byId.isPresent()){
            return null;
        }

        return postRepository.save(post);
    }
    @PostMapping("/update")
    public Post updatePost(@RequestBody Post post){
        Optional<Post> byId = postRepository.findById(post.getPost_id());
        Timestamp rdate = byId.get().getRdate();
        post.setEdate(new Timestamp(System.currentTimeMillis()));
        post.setRdate(rdate);
        return postRepository.save(post);
    }
}
