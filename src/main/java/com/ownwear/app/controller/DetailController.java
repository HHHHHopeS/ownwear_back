package com.ownwear.app.controller;

import com.ownwear.app.service.DetailService;
import com.ownwear.app.vo.PostVo;
import com.ownwear.app.model.Post;
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
    private DetailService service;

    @GetMapping("/{post_id}") // ,"/detail/" permitAll(모두허용) ,authenticated(어떤 권한이든 소유)
    public PostVo getDetail(@PathVariable("post_id") Long post_id){
        return service.getDetail(post_id);
    }

    @PostMapping("/create")
    public Post createPost(@RequestBody Post post){
        return  service.createPost(post);
    }
    @PostMapping("/update")
    public Post updatePost(@RequestBody Post post){
        return service.updatePost(post);
    }

    @GetMapping("/delete")
    public void deletePost(@RequestBody Post post){
        service.deletePost(post);
    }
    @GetMapping("getlist")
    public Page<Post> getList(){
        return service.getList();
    }
}
