package com.ownwear.app.service;

import com.ownwear.app.form.PostCreateForm;
import com.ownwear.app.form.PostForm;
import com.ownwear.app.form.UserInfo;
import com.ownwear.app.model.*;
import com.ownwear.app.repository.*;
import com.ownwear.app.vo.PostVo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

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
    @Autowired
    private LikePostRepository likePostRepository;
    @Autowired
    private HashTagRepository hashTagRepository;

    final ModelMapper modelMapper = new ModelMapper();

    public PostVo getDetail(Long post_id) {

        Optional<Post> byPostno = postRepository.findById(post_id);
        if (byPostno.isPresent()) {
            Post post = byPostno.get();
            return getPostVo(post);
        }
        return null;
    }

    public long createPost(PostCreateForm postCreateForm) {
//        Optional<Post> byId = postRepository.findById(postForm.getPost_id());
//        if (byId.isPresent()) {
//            return null; //todo 에러페이지(잘못된 요청방식)
//        }

        Post post = modelMapper.map(postCreateForm, Post.class);
        Post save = postRepository.save(post);
        List<String> hashtags = postCreateForm.getHashtags();
        for (String hashtagString : hashtags) {
            HashTag hashTag = new HashTag(hashtagString);
            hashTagRepository.save(hashTag);
            PostHashTag postHashTag = new PostHashTag(post,hashTag);
            postHashTagRepository.save(postHashTag);
        }
        return save.getPost_id();
    }

    public PostForm updatePost(PostForm postForm) {
        Optional<Post> byId = postRepository.findById(postForm.getPost_id());
        if (byId.isPresent()) {
            Timestamp rdate = byId.get().getRdate();
            postForm.setEdate(new Timestamp(System.currentTimeMillis()));
            postForm.setRdate(rdate);
            Post map = modelMapper.map(postForm, Post.class);
            return saveAndPostForm(map);
        } else return null;//todo 잘못된 요청방식
    }


    public List<PostForm> getList(int page) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> allByUserIn = postRepository.findAll(pageRequest);
        List<PostForm> postForms = changeToFormList(allByUserIn);
        return postForms;
    }


    public List<PostForm> getList(int page, boolean sex) {
        PageRequest pageRequest = PageRequest.of(page, size);
        User users = new User(sex);
        Page<Post> allByUserIn = postRepository.findAllByUser(users, pageRequest);
        List<PostForm> pp = new ArrayList<>();
        for (Post p : allByUserIn) {
            PostForm postForm = modelMapper.map(p, PostForm.class);
            pp.add(postForm);
        }
        return pp;
    }



    public void deleteById(long post_id) { //todo delete query 직접 작성하여 성능 개선
        System.out.println(post_id);
        postRepository.deleteById(post_id);
    }

    @Transactional(readOnly = true)
    public PostForm mapPostForm(long post_id) {
        Post post = postRepository.findById(post_id).get();
        return modelMapper.map(post, PostForm.class);
    }

    @Transactional(readOnly = true)
    public PostForm saveAndPostForm(Post post){
        Post save = postRepository.save(post);
        return modelMapper.map(save,PostForm.class);
    }

    private List<PostForm> changeToFormList(Iterable<Post> allByUserIn) {
        List<PostForm> postForms = new ArrayList<>();
        for (Post p : allByUserIn) {
            UserInfo userInfo = modelMapper.map(p.getUser(), UserInfo.class);
            PostForm postForm = mapPostForm(p.getPost_id());
            postForm.setUser(userInfo);
            postForms.add(postForm);
        }
        return postForms;
    }
    private PostVo getPostVo(Post post){
        User user = userRepository.findById(post.getUser().getUser_id()).get();
        long likecount = likePostRepository.countByPost(post);
        List<HashTag> hashtags = postHashTagRepository.findHashTagsByPost(post);
        List<PostForm> postForms = changeToFormList(postRepository.findByUser(user));
        List<Comment> comments = commentRepository.findByPost(post);
        PostForm postForm = modelMapper.map(post, PostForm.class);
        PostVo postVo = new PostVo(postForm, likecount, hashtags, postForms, comments, user.getUsername());
        return postVo;
    }
}
