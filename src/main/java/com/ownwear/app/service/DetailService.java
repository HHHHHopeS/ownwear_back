package com.ownwear.app.service;

import com.ownwear.app.form.*;
import com.ownwear.app.model.*;
import com.ownwear.app.repository.*;
import com.ownwear.app.vo.PostVo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private BrandRepository brandRepository;

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
        updateHashtag(post, hashtags);
        return save.getPost_id();
    }

    public PostForm updatePost(PostForm postForm) {
        Optional<Post> byId = postRepository.findById(postForm.getPost_id());

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


    public void deleteById(long post_id) { //todo delete query 직접 작성하여 성능 개선
        //System.out.println(post_id);
        postRepository.deleteById(post_id);
    }


    public IndexForm getIndex() {
        // Rank , Sex , Tag , Brand , 최신
        IndexForm indexForm = new IndexForm();
        indexForm.setPostForms(getPostForms("rank", indexForm.getPostForms())); //랭크기준 포스트 리스트 담기
        indexForm.setUserInfos(getIndexUsers());
        indexForm.setHashTagForms(getPostHashTagInfos());
        indexForm.setBrandForms(getIndexBrands());
//        indexForm.setUserInfos(userRepository.findTop7ByFollow());
//                private List<UserInfo> userInfos;

        return indexForm;
    }


    public Map<String, List<PostForm>> getIndexScroll(String url, int position) {
//        PageRequest pageRequest = PageRequest.of(page, size);
//        Page<Post> allByUserIn = postRepository.findAll(pageRequest);
//        List<PostForm> postForms = changeToFormList(allByUserIn);
        return null;
    }

    private List<IndexUser> getIndexUsers() {
        List<IIndexUser> iIndexUsers = userRepository.findTop7ByFollow();
        List<IndexUser> indexUsers = new ArrayList<>();
        for (IIndexUser iIndexUser : iIndexUsers) {
            IndexUser indexUser = new IndexUser();
            indexUser.setFollow(iIndexUser.getCount());
            indexUser.setUser_id(iIndexUser.getUser_id());
            indexUser.setUsername(iIndexUser.getUserName());
            indexUser.setUserimg(iIndexUser.getUserImg());
            indexUsers.add(indexUser);
        }
        return indexUsers;
    }

    private List<IndexBrand> getIndexBrands() {
        List<IIndexBrand> iIndexBrands = brandRepository.findTop9ByCountByBrand();
        List<IndexBrand> indexBrands = new ArrayList<>();
        for (IIndexBrand iIndexBrand : iIndexBrands) {
            IndexBrand indexBrand = new IndexBrand();
            indexBrand.setTagCount(iIndexBrand.getCount());
            indexBrand.setBrand_id(iIndexBrand.getBrand_id());
            indexBrand.setBrandName(iIndexBrand.getBrandName());
            indexBrands.add(indexBrand);
        }
        return indexBrands;
    }

    private List<IndexHashTag> getPostHashTagInfos() {

        List<IIndexHashTag> iIndexHashTags = postHashTagRepository.findTop9ByCountByHashtagInterface();
        List<IndexHashTag> indexHashTags = new ArrayList<>();
        for (IIndexHashTag iIndexHashTag : iIndexHashTags) {
            IndexHashTag indexHashTag = new IndexHashTag();
            indexHashTag.setCount(iIndexHashTag.getCount());
            indexHashTag.setHashtag_id(iIndexHashTag.getHashtag_id());
            indexHashTag.setHashtagName(iIndexHashTag.getHashtagName());
            indexHashTags.add(indexHashTag);
        }
        return indexHashTags;
    }

    private Map<String, List<PostForm>> getPostForms(String topic, Map<String, List<PostForm>> map) {
        List<PostForm> postFormList = new ArrayList<>();
        switch (topic) {
            case "rank":
                List<LikePost> top6ByPost = likePostRepository.findTop6ByPost();
                for (LikePost likePost : top6ByPost) {
                    LikepostForm likepostForm = modelMapper.map(likePost, LikepostForm.class);
                    PostForm map1 = modelMapper.map(likepostForm.getPost(), PostForm.class);
                    postFormList.add(map1);
                }
            case "sex":

        }

        map.put(topic, postFormList);
        return map;
    }


    @Transactional(readOnly = true)
    public PostForm mapPostForm(long post_id) {
        Post post = postRepository.findById(post_id).get();
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
            PostForm postForm = mapPostForm(p.getPost_id());
            postForm.setUser(userInfo);
            postForms.add(postForm);
        }
        return postForms;
    }

    private PostVo getPostVo(Post post) {
        User user = userRepository.findById(post.getUser().getUser_id()).get();
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
            hashTagRepository.save(hashTag);
            PostHashTag postHashTag = new PostHashTag(post, hashTag);
            postHashTagRepository.save(postHashTag);
        }

    }

    public List<Post> postListPage(int page) {

        Page<Post> postPage = postRepository.findAll(PageRequest.of(page, 12));
        List<Post> posts = postPage.getContent();

        return posts;
    }
}
