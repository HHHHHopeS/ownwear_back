package com.ownwear.app.controller;

import com.ownwear.app.dto.PostVo;
import com.ownwear.app.model.Comment;
import com.ownwear.app.model.Post;
import com.ownwear.app.repository.CommentRepository;
import com.ownwear.app.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("detail")
public class DetailController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("get")
    public PostVo getDetail(long postno){
        Optional<Post> byPostno = postRepository.findByPostno(postno);
        if (byPostno.isPresent()){
            PostVo postVo = new PostVo();
            Post post = byPostno.get();
            ArrayList<Comment> postComments = commentRepository.findByPostno(postno);
            if (!postComments.isEmpty()){

            }
//            postVo.set
        }
        return null;
    }

}
