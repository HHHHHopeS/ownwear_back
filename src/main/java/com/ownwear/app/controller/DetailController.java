package com.ownwear.app.controller;

import com.ownwear.app.model.HashTag;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.vo.PostVo;
import com.ownwear.app.model.Comment;
import com.ownwear.app.model.Post;
import com.ownwear.app.repository.CommentRepository;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.vo.UserRelatedVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/detail")
public class DetailController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{postno}")
    public PostVo getDetail(@PathVariable("postno") Long postno){

        Optional<Post> byPostno = postRepository.findByPostno(postno);
        if (byPostno.isPresent()){
            Post post = byPostno.get();
            System.out.println(userRepository.findById(post.getId()));
            String username = userRepository.findById(post.getId()).get().getUsername();
            int likecount = 1;
            ArrayList<HashTag> hashtags = null;
            ArrayList<UserRelatedVo> userRelated = null;
            ArrayList<Comment> comments = commentRepository.findByPostno(postno);

            PostVo postVo = new PostVo(post,likecount,hashtags,userRelated,comments,username);
            return postVo;
        }
        return null;
    }

}
