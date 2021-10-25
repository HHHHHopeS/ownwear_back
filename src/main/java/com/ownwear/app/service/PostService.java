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
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
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

    public long createPost(PostForm postForm) {
//        Optional<Post> byId = postRepository.findById(postForm.getPostid());
//        if (byId.isPresent()) {
//            return null; //todo 에러페이지(잘못된 요청방식)
//        }

        Post post = modelMapper.map(postForm, Post.class);
        Post save = postRepository.save(post);
        List<String> hashtags = postForm.getHashtags();
        List<String> brands = postForm.getBrands();
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


    public boolean deleteById(long postid) { //todo delete query 직접 작성하여 성능 개선
        //System.out.println(postid);
        try {

            postRepository.deleteById(postid);
            return true;
        } catch (Exception e) {
            return false;
        }

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

    public Page getPostList(String type, String value, int page) {
        PageRequest pageRequest = PageRequest.of(page, 12);
        switch (type) {
            case "brand":
                Page<IndexPost> byCountByBrand = postBrandRepository.findByCountByBrand(value, pageRequest)
                        .map(iIndexPost -> {

                            IndexPost indexPost = new IndexPost();
                            indexPost.setPostid(iIndexPost.getPostid());
                            indexPost.setImgdata(iIndexPost.getImgdata());
                            indexPost.setUser(modelMapper.map(iIndexPost.getUser(), UserInfo.class));
                            return indexPost;
                        });
                return byCountByBrand;
            case "hashtag":
                Page<IndexPost> byCountByHashTag = postHashTagRepository.findPostHashTagsByHashtag(hashTagRepository.findByHashtagname(value).get(), pageRequest).map(
                        postHashTag -> {
                            IndexPost indexPost = new IndexPost();
                            indexPost.setPostid(postHashTag.getPost().getPostid());
                            indexPost.setImgdata(postHashTag.getPost().getImgdata());
                            indexPost.setUser(modelMapper.map(postHashTag.getPost().getUser(), UserInfo.class));
                            return indexPost;
                        }

                );
                System.out.println(byCountByHashTag);

                return byCountByHashTag;
        }
        return null;
    }


    public boolean checkIsLike(long userid, long postid) {
        Optional<User> userOPt = userRepository.findById(userid);
        User user = userOPt.get();
        Optional<Post> postOpt = postRepository.findById(postid);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            return likePostRepository.findByUserAndPost(user, post).isPresent();
        }
        return false;
    }


// 랭킹페이지 리스트 불러오기
// type = like or brand or user
// filter = all or man or women ** brand filter always null
// count = page count

// return
// like.content = [post]
// user.content = [userinfo] 팔로잉 팔로워 카운트 포함 유저 프로필 subnav데이터 불러올때랑 완죤 똑같은 데이터유형
// brand.content = [brandinfo]
// brandinfo = {
//     brandname:,
//     postedcount: 브랜드 태깅한 포스트 카운트,
//     [post]: 태깅한 포스트 단 3개 순서는 최신순이던 인기순이던 상관 무,
//     }

    public List getRankingData(String type, String filter, int page, Long current_userid) {

        PageRequest pageRequest = PageRequest.of(page, 10);
        User currentUser = null;
        if (current_userid != -1) {
            currentUser = userRepository.findById(current_userid).get();
        }
        switch (type) {
            case "likes":
                return postRepository.findRankingData(filter, pageRequest).getContent().stream().map(post -> modelMapper.map(post, PostForm.class)).collect(Collectors.toList());
            case "brand":
                List<BrandInfo> brandInfos = new ArrayList<>();
                boolean sex = filter.equals("men");
                List<BrandInfo> rankingData = brandRepository.findRankingData(sex, pageRequest).getContent()
                        .stream().map(iIndexBrand -> {
                            BrandInfo brandInfo = new BrandInfo();

//                            System.out.println(brandRepository.findAllByBrandid(iIndexBrand.getBrandid()).stream().map(post -> {System.out.println(post); return post;}));
                            List<Post> allByBrandid = brandRepository.findAllByBrandid(iIndexBrand.getBrandid());
                            for (Post post : allByBrandid) {
                                brandInfo.getPosts().add(modelMapper.map(post, PostForm.class));
                            }
                            brandInfo.setPosts((List) brandInfo.getPosts().stream().distinct().collect(Collectors.toList()));
                            brandInfo.setPostcount(iIndexBrand.getCounts());
                            brandInfo.setBrandname(iIndexBrand.getBrandName());
                            return brandInfo;
                        }).collect(Collectors.toList());
                BrandInfo brandInfo = new BrandInfo();

                brandInfos.add(brandInfo);
                return rankingData;
            case "user":
                User finalCurrentUser = currentUser;
                List<UserProfile> collect = userRepository.findRankingData(filter, pageRequest).getContent()
                        .stream().map(user -> setUserProfile(user, finalCurrentUser)).collect(Collectors.toList());//todo current_userid

                return collect;
            default:
                return null;
        }
    }

    private UserProfile setUserProfile(User user, User currentUser) {
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(modelMapper.map(user, UserInfo.class));
        userProfile.setFollower(followRepository.countByTouser(user));
        userProfile.setFollowing(followRepository.countByFromuser(user));
        User targetUser = modelMapper.map(userProfile.getUser(), User.class);
        boolean present = followRepository.findByUsers(currentUser, targetUser).isPresent();
        userProfile.getUser().setIsfollowing(present);
        return userProfile;
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


}


