package com.ownwear.app.controller;

import com.ownwear.app.model.HashTag;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.vo.PostVo;
import com.ownwear.app.model.Comment;
import com.ownwear.app.model.Post;
import com.ownwear.app.repository.CommentRepository;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.vo.UserRelatedVo;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    public PostVo getDetail(@PathVariable("postno") long postno){

        Optional<Post> byPostno = postRepository.findByPostno(postno);

        if (byPostno.isPresent()){

            Post post = byPostno.get();

            Map<String, Object> imgdata = post.getImgdata();
            imgdata= new HashMap<>();
            imgdata.put("asd","Asd");
            imgdata.put("assd","Assd");
                    post.setImgdata(imgdata);
            System.out.println(userRepository.findById(post.getId()));

            String username = userRepository.findById(post.getId()).get().getUsername();

            postRepository.save(post);
            int likecount = 1;

            ArrayList<HashTag> hashtags = null;

            ArrayList<UserRelatedVo> userRelated = null;

            ArrayList<Comment> comments = commentRepository.findByPostno(postno);

            PostVo postVo = new PostVo(post,likecount,hashtags,userRelated,comments,username);

            return postVo;
        }
        return null;
    }

    @PostMapping("/save")
    public PostVo savePost(@RequestBody PostVo postVo){

        return postVo;
    }
}
