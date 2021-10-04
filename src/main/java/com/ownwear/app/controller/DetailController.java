package com.ownwear.app.controller;

import com.ownwear.app.form.PostForm;
import com.ownwear.app.model.User;
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
    @GetMapping("getlist")
    public List<PostForm> getList(int page){
        return service.getList(page);
    }@GetMapping("getlist/man")
    public List<PostForm> getManList(int page){
        return service.getList(page,true);
    }@GetMapping("getlist/woman")
    public List<PostForm> getWomanList(int page){
        return service.getList(page,false);
    }
}