package com.ownwear.app.controller;

import com.ownwear.app.dto.*;
import com.ownwear.app.entity.Post;
import com.ownwear.app.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public PostVo getDetail(@PathVariable("postid") long postid){
        return service.getDetail(postid);
    }


    @PostMapping("/create")//,"detail/create/**" authenicated
    public long createPost(@RequestBody PostCreateForm post){
        //        //System.out.println(post);
        return  service.createPost(post);
    }
    @PostMapping("/update")//,"detail/update/**" authentticated
    public PostForm updatePost(@RequestBody PostForm post){
        return service.updatePost(post);
    }


    @PostMapping("/delete")//,"detail/delete/**" authenicated
    public void deletePost(long postid){
        service.deleteById(postid);
    }

//    @GetMapping("getlist/man")
//    public List<PostForm> getManList(int page){
//        return service.getList(page,true);
//    }
//
//    @GetMapping("getlist/woman")
//    public List<PostForm> getWomanList(int page){
//        return service.getList(page,false);
//    }


    @PostMapping("/postlist")
    public  List<PostForm> getPostList(@RequestBody UserForm user) {

        PageRequest pageRequest = PageRequest.of(0,10);
        List<PostForm> postList = service.getPostList(user, pageRequest);

        return postList;
    }
//
//    @GetMapping("/postlist/{id}")
//    public List<Post> getPostListPage(@PathVariable("id") int page) {
//
//        List<Post> posts = service.postListPage(page);
//
//        return posts;
//    }

    @GetMapping("post/profile")
    public UserInfo getPostProfile(Long current_userid, Long postid){
        return service.getPostUser(current_userid,postid);
    }

    //,"detail/post/brand" permit all
    @GetMapping("/post/brand") //브랜드 리스트
    public Page<IndexPost> getPostByBrand(Pageable pageable) {

        Page<IndexPost> postByBrand = service.getPostByBrand(pageable);

        return postByBrand;
    }

    //,"detail/post/tag" permit all
    @GetMapping("/post/tag") //태그 리스트
    public Page<IndexPost> getPostByTag(Pageable pageable) {

        Page<IndexPost> postByTag = service.getPostByTag(pageable);

        return postByTag;
    }
}