package com.ownwear.app.service;

import com.ownwear.app.form.*;
import com.ownwear.app.model.*;
import com.ownwear.app.repository.*;
import com.ownwear.app.form.PostVo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PostService {

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
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private PostBrandRepository postBrandRepository;


    final ModelMapper modelMapper = new ModelMapper();


    public PostVo getDetail(Long postid) {

        Optional<Post> byPostno = postRepository.findById(postid);
        if (byPostno.isPresent()) {
            Post post = byPostno.get();
            return getPostVo(post);
        }
        return null;
    }

    public long createPost(PostCreateForm postCreateForm) {
//        Optional<Post> byId = postRepository.findById(postForm.getPostid());
//        if (byId.isPresent()) {
//            return null; //todo 에러페이지(잘못된 요청방식)
//        }

        Post post = modelMapper.map(postCreateForm, Post.class);
        Post save = postRepository.save(post);
        List<String> hashtags = postCreateForm.getHashtags();
        List<String> brands = postCreateForm.getBrands();
        updateHashtag(post, hashtags);
        updateBrand(post, brands);
        return save.getPostid();
    }

    public PostForm updatePost(PostForm postForm) {
        Optional<Post> byId = postRepository.findById(postForm.getPostid());

        if (byId.isPresent()) {
            Timestamp rdate = byId.get().getRdate();
            postForm.setEdate(new Timestamp(System.currentTimeMillis()));
            postForm.setRdate(rdate);
            Post post = modelMapper.map(postForm, Post.class);
            List<String> hashtags = postForm.getHashtags();
            postHashTagRepository.deleteAllByPost(post);
            updateHashtag(post, hashtags);
            return saveAndPostForm(post);
        } else return null;//todo 잘못된 요청방식(해당 게시글이 존재하지 않습니다.)
    }


    public List<PostForm> getList(int page, boolean sex) {
        PageRequest pageRequest = PageRequest.of(page, size);
        User user = new User(sex);
        Page<Post> allByUserIn = postRepository. findAllByUser(user, pageRequest);
        List<PostForm> pp = new ArrayList<>();
        for (Post p : allByUserIn) {
            PostForm postForm = modelMapper.map(p, PostForm.class);
            pp.add(postForm);
        }
        return pp;
    }


    public void deleteById(long postid) { //todo delete query 직접 작성하여 성능 개선
        //System.out.println(postid);
        postRepository.deleteById(postid);
    }

    @Transactional(readOnly = true)
    public PostForm mapPostForm(long postid) {
        Post post = postRepository.findById(postid).get();
        return modelMapper.map(post, PostForm.class);
    }

    @Transactional(readOnly = true)
    public PostForm saveAndPostForm(Post post) {
        Post save = postRepository.save(post);
        return modelMapper.map(save, PostForm.class);
    }

    private List<PostForm> changeToFormList(Iterable<Post> allByUserIn) {
        List<PostForm> postForms = new ArrayList<>();
        for (Post p : allByUserIn) {
            UserInfo userInfo = modelMapper.map(p.getUser(), UserInfo.class);
            PostForm postForm = mapPostForm(p.getPostid());
            postForm.setUser(userInfo);
            postForms.add(postForm);
        }
        return postForms;
    }

    private PostVo getPostVo(Post post) {
        User user = userRepository.findById(post.getUser().getUserid()).get();
        long likecount = likePostRepository.countByPost(post);
        List<HashTag> hashtags = postHashTagRepository.findHashTagsByPost(post);
        List<PostForm> postForms = changeToFormList(postRepository.findByUser(user));
        List<Comment> comments = commentRepository.findByPost(post);
        PostForm postForm = modelMapper.map(post, PostForm.class);
        PostVo postVo = new PostVo(postForm, likecount, hashtags, postForms, comments, user.getUsername());
        return postVo;
    }

    private void updateHashtag(Post post, List<String> hashtags) {
        for (String hashtagString : hashtags) {
            HashTag hashTag = new HashTag(hashtagString);
            Optional<HashTag> byHashtagname = hashTagRepository.findByHashtagname(hashtagString);
            if (byHashtagname.isPresent()) {
                hashTag = byHashtagname.get();
            } else {
                hashTagRepository.save(hashTag);
            }
            PostHashTag postHashTag = new PostHashTag(post, hashTag);
            postHashTagRepository.save(postHashTag);
        }
    }

    private void updateBrand(Post post, List<String> brands) {
        for (String brandString : brands) {
            Brand brand = new Brand(brandString);
            Optional<Brand> byBrandname = brandRepository.findByBrandname(brandString);
            if (byBrandname.isPresent()) {
                brand = byBrandname.get();
            } else {
                brandRepository.save(brand);
            }
            PostBrand postBrand = new PostBrand(post, brand);
            postBrandRepository.save(postBrand);
        }
    }

    public List<PostForm> getPostList(UserForm userForm, Pageable pageable) {

        User user = modelMapper.map(userForm,User.class);

        Page<Post> allByUser = postRepository.findAllByUser(user, pageable);

        List<PostForm> pp = new ArrayList<>(); //todo 클린코딩으로 바꾸기 (한줄)

        for (Post p : allByUser) {
            PostForm postForm = modelMapper.map(p, PostForm.class);
            pp.add(postForm);
        }

        List<Post> content = allByUser.getContent();

        return pp;
    }


}
