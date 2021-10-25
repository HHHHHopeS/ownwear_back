package com.ownwear.app.controller;

import com.ownwear.app.dto.*;
import com.ownwear.app.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detail") //todo post로 바꾸기
@Slf4j
public class PostController {

    @Autowired
    private PostService service;

    @GetMapping("/{postid}") // ,"/detail/" permitAll(모두허용) ,authenticated(어떤 권한이든 소유)
    public PostVo getDetail(@PathVariable("postid") long postid) {
        return service.getDetail(postid);
    }

    @GetMapping("/like/check")
    public boolean isLike(long userid, long postid) {

        return service.checkIsLike(userid, postid);

    }


    @PostMapping("/create")//,"detail/create/**" authenicated
    public long createPost(@RequestBody PostForm post) {
        System.out.println(post);
        return service.createPost(post);
    }

    @PostMapping("/update")//,"detail/update/**" authentticated
    public PostForm updatePost(@RequestBody PostForm post) {
        return service.updatePost(post);
    }


    @GetMapping("/delete")//,"detail/delete/**" authenicated
    public Boolean deletePost(Long postid) {

        return service.deleteById(postid);
    }

    @GetMapping("/postlist")
    public Object getPostList(String type, int page, String value) {


        return service.getPostList(type, value, page);

    }

    @GetMapping("post/profile")
    public UserInfo getPostProfile(Long current_userid, Long postid) {
        return service.getPostUser(current_userid, postid);
    }

    @GetMapping("ranking")
    public List getRankingData(String type, String filter, int page, Long current_userid) {
        return service.getRankingData(type, filter, page, current_userid);
    }
}




