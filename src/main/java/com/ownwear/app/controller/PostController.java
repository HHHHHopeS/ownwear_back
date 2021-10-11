package com.ownwear.app.controller;

import com.ownwear.app.form.IndexForm;
import com.ownwear.app.form.PostCreateForm;
import com.ownwear.app.form.PostForm;
import com.ownwear.app.model.Post;
import com.ownwear.app.service.DetailService;
import com.ownwear.app.vo.PostVo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/detail")
@Slf4j
public class PostController {

    @Autowired
    private DetailService service;

    @GetMapping("/{post_id}") // ,"/detail/" permitAll(모두허용) ,authenticated(어떤 권한이든 소유)
    public PostVo getDetail(@PathVariable("post_id") long post_id){
        return service.getDetail(post_id);
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
    public void deletePost(long post_id){
        service.deleteById(post_id);
    }

    @GetMapping("/getindex")
    public IndexForm getIndex(){
        System.out.println("getIndex 입장");
        return service.getIndex();
    }
    @GetMapping("/getlist")
    public Map<String, List<PostForm>> getIndexScroll(String url , int position){
        return service.getIndexScroll(url, position);
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


    @RequestMapping("/hashtagAutoComplete")
    private static JSONArray hashtagAutoComplete(@RequestBody String data){
        String[] str = {"assdf","asdfwefewf","asfwefew"};

        JSONArray arr = JSONArray.fromObject(str);
        return arr;
    }

    @GetMapping("/postlist/{id}")
    public List<Post> getPostListPage(@PathVariable("id") int page) {

        List<Post> posts = service.postListPage(page);

        return posts;
    }

}