package com.ownwear.app.service;

import com.ownwear.app.form.PostForm;
import com.ownwear.app.form.UserInfo;
import com.ownwear.app.model.Comment;
import com.ownwear.app.model.HashTag;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.CommentRepository;
import com.ownwear.app.repository.PostHashTagRepository;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.vo.PostVo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;

@Service
public class DetailService {

    final int size = 12;

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostHashTagRepository postHashTagRepository;

    final ModelMapper modelMapper = new ModelMapper();

    public PostVo getDetail(Long post_id) {

        Optional<Post> byPostno = postRepository.findById(post_id);
        if (byPostno.isPresent()){
            Post post = byPostno.get();
            System.out.println(userRepository.findById(post.getPost_id()));

            User user = userRepository.findById(post.getPost_id()).get();
            int likecount = 1;
            ArrayList<HashTag> hashtags = postHashTagRepository.findByPost(post);
            ArrayList<Post> userRelated = postRepository.findByUser(user);
            ArrayList<Comment> comments = commentRepository.findByPost(post);

            PostVo postVo = new PostVo(post,likecount,hashtags,userRelated,comments,user.getUsername());
            return postVo;

        }
        return null;
    }

    public Post createPost(Post post) {
        Optional<Post> byId = postRepository.findById(post.getPost_id());
        if (byId.isPresent()){
            return null;
        }

        return postRepository.save(post);
    }

    public Post updatePost(Post post) {
        Optional<Post> byId = postRepository.findById(post.getPost_id());
        Timestamp rdate = byId.get().getRdate();
        post.setEdate(new Timestamp(System.currentTimeMillis()));
        post.setRdate(rdate);
        return postRepository.save(post);
    }

    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    public List<PostForm> getList(int page) {
        PageRequest pageRequest= PageRequest.of(0,size);
        Page<Post> allByUserIn = postRepository.findAll(pageRequest);
        List<PostForm> pp = new ArrayList<>();
        for (Post p : allByUserIn){
            System.out.println(p.getUser());
            UserInfo userInfo= modelMapper.map(p.getUser(), UserInfo.class);
            PostForm postForm = getPostForm(p.getPost_id());
            postForm.setUser(userInfo);
            pp.add(postForm);
        }
        System.out.println(pp.get(3).getUser());
        return pp;
    }
public List<PostForm> getList(int page,boolean sex) {
        List<User> user = userRepository.findAllBySex(false);
        System.out.println(user);
        PageRequest pageRequest= PageRequest.of(0,size);
        System.out.println(22222222222222L);
        User users = new User(false);
        Page<Post> allByUserIn = postRepository.findAllByUser(users, pageRequest);
        System.out.println(allByUserIn);
        List<PostForm> pp = new ArrayList<>();
        for (Post p : allByUserIn){
            PostForm postForm = getPostForm(p.getPost_id());
            pp.add(postForm);
        }
        System.out.println(pp.get(3).getUser());
        return pp;
    }



    @Transactional(readOnly = true)
    public PostForm getPostForm(long post_id){
        Post post= postRepository.findById(post_id).get();
        return modelMapper.map(post,PostForm.class);
    }
}
