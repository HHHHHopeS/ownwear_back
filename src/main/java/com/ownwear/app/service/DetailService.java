package com.ownwear.app.service;

import com.ownwear.app.model.Comment;
import com.ownwear.app.model.HashTag;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.CommentRepository;
import com.ownwear.app.repository.PostHashTagRepository;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public Page<Post> getList() {
        List<User> user = userRepository.findAllBySex(false );
        System.out.println(user);
        PageRequest pageRequest= PageRequest.of(0,size);
        System.out.println(22222222222222L);
        return postRepository.findAllByUserIn(user,pageRequest);
    }
}
