package com.ownwear.app.service;

import com.ownwear.app.dto.*;
import com.ownwear.app.entity.*;
import com.ownwear.app.repository.*;
import com.ownwear.app.dto.PostVo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {

    final int size = 12;


    private PostRepository postRepository;

    private CommentRepository commentRepository;

    private UserRepository userRepository;

    private PostHashTagRepository postHashTagRepository;

    private LikePostRepository likePostRepository;

    private HashTagRepository hashTagRepository;

    private BrandRepository brandRepository;

    private PostBrandRepository postBrandRepository;


    final ModelMapper modelMapper = new ModelMapper();
    private FollowRepository followRepository;


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
        Page<Post> allByUserIn = postRepository.findAllByUser(user, pageRequest);
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
        List<HashTagForm> hashtags = postHashTagRepository.findHashTagsByPost(post).stream().map(HashTag -> modelMapper.map(HashTag, HashTagForm.class)).collect(Collectors.toList());
        List<PostForm> postForms = changeToFormList(postRepository.findByUser(user));
        List<CommentForm> comments = commentRepository.findAllByPost(post).stream().map(Comment -> modelMapper.map(Comment, CommentForm.class)).collect(Collectors.toList());
        ;
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
        User user = modelMapper.map(userForm, User.class);
        Page<Post> allByUser = postRepository.findAllByUser(user, pageable);

        List<PostForm> pp = new ArrayList<>(); //todo 클린코딩으로 바꾸기 (한줄)

        for (Post p : allByUser) {
            PostForm postForm = modelMapper.map(p, PostForm.class);
            pp.add(postForm);
        }

        List<Post> content = allByUser.getContent();

        return pp;
    }

    public UserInfo getPostUser(Long current_userid, Long postid) {
        Optional<Post> byId = postRepository.findById(postid);
        if (byId.isPresent()) {
            Post post = byId.get();
            User user = post.getUser();
            User currentUser = userRepository.findById(current_userid).get();
            UserInfo userInfo = modelMapper.map(user, UserInfo.class);
            userInfo.setIsfollowing(followRepository.findByUsers(currentUser, user).isPresent());
            return userInfo;
        }
        return null; //todo 잘못된 요청 존재하지않는 포스트
    }

    public Page<IndexPost> getPostByBrand(Pageable pageable) { //브랜드 리스트 불러오기

        Page<IndexPost> byCountByBrand = postBrandRepository.findByCountByBrand("나이키", pageable)
                .map(iIndexPost -> {
                    IndexPost indexPost = new IndexPost();
                    indexPost.setPostid(iIndexPost.getPostid());
                    indexPost.setImgData(iIndexPost.getImgdata());
                    indexPost.setUser(modelMapper.map(iIndexPost.getUser(), UserInfo.class));
                    return indexPost;
                });

        return byCountByBrand;
    }
}
