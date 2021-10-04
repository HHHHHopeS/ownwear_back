package com.ownwear.app.controller;

<<<<<<< Updated upstream
import com.ownwear.app.form.PostForm;
import com.ownwear.app.model.User;
import com.ownwear.app.service.DetailService;
import com.ownwear.app.vo.PostVo;
import com.ownwear.app.model.Post;
=======
import com.ownwear.app.model.*;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.vo.PostVo;
import com.ownwear.app.repository.CommentRepository;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.vo.UserRelatedVo;
>>>>>>> Stashed changes
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/detail")
@Slf4j
public class DetailController {

    @Autowired
<<<<<<< Updated upstream
    private DetailService service;
=======
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;
>>>>>>> Stashed changes

    @GetMapping("/{post_id}") // ,"/detail/" permitAll(모두허용) ,authenticated(어떤 권한이든 소유)
    public PostVo getDetail(@PathVariable("post_id") Long post_id){
        return service.getDetail(post_id);
    }


    @PostMapping("/create")//,"detail/create/**" authenicated
    public PostForm createPost(@RequestBody PostForm postForm){

        return  service.createPost(postForm);
    }
    @PostMapping("/update")//,"detail/update/**" authentticated
    public PostForm updatePost(@RequestBody PostForm postForm){
        return service.updatePost(postForm);
    }

    @GetMapping("/delete")//,"detail/delete/**" authenicated
    public void deletePost(long post_id){
        service.deleteById(post_id);
    }
<<<<<<< Updated upstream
    @GetMapping("getlist")
    public List<PostForm> getList(int page){
        return service.getList(page);
    }@GetMapping("getlist/man")
    public List<PostForm> getManList(int page){
        return service.getList(page,true);
    }@GetMapping("getlist/woman")
    public List<PostForm> getWomanList(int page){
        return service.getList(page,false);
=======

    @PostMapping("/update")
    public Post updatePost(@RequestBody Post post){
        Optional<Post> byId = postRepository.findById(post.getPost_id());
        Timestamp rdate = byId.get().getRdate();
        post.setEdate(new Timestamp(System.currentTimeMillis()));
        post.setRdate(rdate);
        return postRepository.save(post);
>>>>>>> Stashed changes
    }
}