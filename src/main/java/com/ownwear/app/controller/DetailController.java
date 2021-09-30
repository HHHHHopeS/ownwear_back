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

import java.util.ArrayList;
import java.util.List;
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

    @GetMapping("/{id}")
    public PostVo getDetail(@PathVariable("id") Long id){
        //log.info("들어온값 {}", id);
        Optional<Post> byPostno = postRepository.findById(id);
        if (byPostno.isPresent()){
            Post post = byPostno.get();
            System.out.println(userRepository.findById(post.getPost_id()));
            String username = userRepository.findById(post.getPost_id()).get().getUsername();
            int likecount = 1;
            ArrayList<HashTag> hashtags = null;
            ArrayList<UserRelatedVo> userRelated = null;
            ArrayList<Comment> comments = commentRepository.findBypost(post);

            PostVo postVo = new PostVo(post,likecount,hashtags,userRelated,comments,username);
            return postVo;
        }
        return null;
    }

}
