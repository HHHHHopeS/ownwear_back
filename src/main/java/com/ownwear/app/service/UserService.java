package com.ownwear.app.service;


import com.ownwear.app.controller.VisionController;
import com.ownwear.app.exception.ResourceNotFoundException;
import com.ownwear.app.dto.*;
import com.ownwear.app.entity.*;
import com.ownwear.app.repository.*;
import com.ownwear.app.security.UserPrincipal;
import lombok.AllArgsConstructor;
import net.sf.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final ModelMapper modelMapper = new ModelMapper();

    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private CurrentUsersRepository currentUsersRepository;
    private AlertRepository alertRepository;
    private CommentRepository commentRepository;
    private LikePostRepository likePostRepository;
    private FollowRepository followRepository;

    public UserProfile getUserDetail(String username, Long current_userid) {
        Optional<User> tagetUserOp = userRepository.findByUsername(username);
        UserProfile targetUserProfile = new UserProfile();
        Optional<User> currentUserOp = null;
        if (current_userid != -1) {
            currentUserOp = userRepository.findById(current_userid);
            if (tagetUserOp.isPresent()) {
                User targetUser = tagetUserOp.get();
                User currentUser = currentUserOp.get();
                UserInfo targetUserInfo = modelMapper.map(targetUser, UserInfo.class);
                targetUserProfile.setUser(targetUserInfo);
                boolean present = followRepository.findByUsers(currentUser, targetUser).isPresent();
                targetUserProfile.getUser().setIsfollowing(present);
                return setUserProfile(targetUserProfile, targetUser);
            } else return null;
            //id로 해당 유저 찾을수 없음
        } else { //현재 비회원유저
            if (tagetUserOp.isPresent()) {
                User targetUser = tagetUserOp.get();
                UserInfo targetUserInfo = modelMapper.map(targetUser, UserInfo.class);
                targetUserProfile.setUser(targetUserInfo);
                targetUserProfile.getUser().setIsfollowing(false);
                return setUserProfile(targetUserProfile, targetUser);
            } else return null;
            //id로 해당 유저 찾을수 없음
        }
    }

    private UserProfile setUserProfile(UserProfile targetUserProfile, User targetUser) {

        targetUserProfile.setFollower(followRepository.countByTouser(targetUser));
        targetUserProfile.setFollowing(followRepository.countByFromuser(targetUser));
        return targetUserProfile;
    }


    public Page<IndexPost> getUserPosts(String username, int page) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        PageRequest pageRequest = PageRequest.of(page, 12, Sort.by("rdate").descending());
        if (byUsername.isPresent()) {
            Page<IndexPost> indexPosts = postRepository.findAllByUser(byUsername.get(), pageRequest)
                    .map(Post -> modelMapper.map(Post, IndexPost.class));

            indexPosts.forEach(indexPost -> setIndexPosts(indexPost));
//            indexPosts = (Page<IndexPost>) setIndexPosts(indexPosts);

            return indexPosts;
        }
        return null;
    }

    private void setIndexPosts(IndexPost indexPost) {
        indexPost.setCommentcount(commentRepository.countAllByPostPostid(indexPost.getPostid()));
        indexPost.setLikecount(likePostRepository.countByPostPostid(indexPost.getPostid()));

    }

    private List<IndexPost> getIndexPosts(List<IIndexPost> iIndexPosts) {
        List<IndexPost> indexPosts = new ArrayList<>();
        for (IIndexPost iIndexPost : iIndexPosts) {
            IndexPost indexPost = new IndexPost();
            indexPost.setPostid(iIndexPost.getPostid());
            indexPost.setImgdata(iIndexPost.getImgdata());
            indexPost.setRdate(iIndexPost.getRdate());
            indexPosts.add(indexPost);
        }
        return indexPosts;
    }

    private List<IndexPost> getIndexPosts(Page<Post> posts) {
        List<IndexPost> indexPosts = new ArrayList<>();
        for (Post post : posts) {
            IndexPost indexPost = modelMapper.map(post, IndexPost.class);
            indexPosts.add(indexPost);
        }
        return indexPosts;
    }

    public UserInfo getCurrentUser(UserPrincipal userPrincipal, HttpServletRequest request) {

        User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        //System.out.println(user);
        Optional<CurrentUsers> byUser = currentUsersRepository.findByUser(user);

        String requestToken = request.getHeader("Authorization");

        if (requestToken != null && requestToken.startsWith("Bearer")) {
            requestToken = requestToken.substring(7);
        }
        if (byUser.isPresent()) {
            CurrentUsers currentUsers = byUser.get();
            if (currentUsers.getToken().equals(requestToken)) {

                return checkAndReturn(user);
            }
            return null; //todo 유효하지않은 토큰입니다.
        } else {
            //System.out.println("##최초접속 userid : " + user.getUserid());
            CurrentUsers currentUsers = new CurrentUsers(user, requestToken);
            currentUsersRepository.save(currentUsers);
            return checkAndReturn(user);
        }
    }

    private UserInfo checkAndReturn(User user) {

        UserInfo userInfo = modelMapper.map(user, UserInfo.class);
        List<Alert> falseByUser = alertRepository.findFalseByUser(user);

        if (falseByUser.isEmpty()) {
            userInfo.setIschecked(true);
        } else {
            userInfo.setIschecked(false);
        }
        return userInfo;
    }

    public boolean validationCheck(String value) { //중복값 체크

        boolean contains = value.contains("@");

        if (contains) { //이메일
            Optional<User> byEmail = userRepository.findByEmail(value);
            if (byEmail.isPresent()) {
                return false;
            } else return true;
        } else { //username
            Optional<User> byUsername = userRepository.findByUsername(value);
            if (byUsername.isPresent()) {
                return false;
            } else return true;
        }
    }

    public boolean addInfoOauth2(UserInfo userInfo) {

        try {
            Optional<User> byId = userRepository.findById((userInfo.getUserid()));
            if (byId.isPresent()) {
                User user = modelMapper.map(userInfo, User.class);
                user.setIsverified(true);
                user.setProviderid("facebook");
                user.setProvider("facebook");
                userRepository.save(user);

                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    public UserForm getUserData(Long userid) {

        Optional<User> byId = userRepository.findById(userid);

        if (byId.isPresent()) {
            UserForm map = modelMapper.map(byId.get(), UserForm.class);
            return map;
        }

        return null;

    }


    public Boolean updateUser(UserInfo userInfo) {

        Optional<User> byUsername = userRepository.findByUsername(userInfo.getUsername());
        Optional<User> byEmail = userRepository.findByEmail(userInfo.getEmail());

        if (!byUsername.isPresent()) {

            return false;
        } else if (!byEmail.isPresent()) {
            return false;
        }
        putUserinfo(userInfo);
        return true;
    }

    public void putUserinfo(UserInfo userInfo) {

        userRepository.updateUser(userInfo.getUserid(), userInfo.getHeight(), userInfo.getInstaid(), userInfo.getTwitterid(), userInfo.getPinterestid(), userInfo.getUserimg());
    }

    public UserInfo updateUserImg(UserInfo userInfo) throws IOException {
        Optional<User> byUsername = userRepository.findByUsername(userInfo.getUsername());
        Optional<User> byEmail = userRepository.findByEmail(userInfo.getEmail());

        if (!byUsername.isPresent()) {

            return null;
        } else if (!byEmail.isPresent()) {
            return null;
        }
        if (userInfo.getUserimg() != null) {
            JSONObject jsonObject = VisionController.uploadImage(userInfo.getUserimg());
            String data = (String) jsonObject.get("data");
            userInfo.setUserimg(data);
            putUserinfo(userInfo);

        } else {
            userInfo.setUserimg(null);
            putUserinfo(userInfo);
        }
        return userInfo;
    }


    public Boolean updatePw(String pw, Long id, String newPw) {

        if (passwordEncoder.matches(pw, userRepository.findById(id).get().getPassword())) {

            userRepository.updatePw(id, passwordEncoder.encode(newPw));


            return true;
        }
        return false;
    }

    public boolean checkPw(String pw, Long id) {

        return passwordEncoder.matches(pw, userRepository.findById(id).get().getPassword());
    }

    public List<ListModalForm> getListModal(ListModalRequest request) {


        Optional<User> byId = userRepository.findById(request.getCurrent_userid());
        User currentUser = null;
        Boolean isTrue = false;
        if (byId.isPresent()) {
            currentUser = byId.get();
        }
        String type = request.getType();

        List<ListModalForm> listModalForms = new ArrayList<>();

        if (type.equals("like")) {
            Optional<Post> targetPostById = postRepository.findById(request.getTargetid());
            if (targetPostById.isPresent()) {
                System.out.println("모달1");
                Post target = targetPostById.get();
                List<LikePostForm> byUserAndPost = likePostRepository.findAllByPost(target)
                        .stream().map(LikePost -> modelMapper.map(LikePost, LikePostForm.class)).collect(Collectors.toList());
                for (LikePostForm likePostForm : byUserAndPost) {
                    ListModalForm listModalForm = new ListModalForm();
                    User user = userRepository.findById(likePostForm.getUser().getUserid()).get();
                    if (currentUser != null) {
                        System.out.println("모달2");
                        Optional<Follow> isFollowing = followRepository.findByUsers(currentUser, user);
                        isTrue = isFollowing.isPresent();
                    }
                    listModalForm.setUser(modelMapper.map(user, UserForm.class));
                    listModalForm.setIsTrue(isTrue);
                    listModalForm.setFollower(followRepository.countByTouser(user));
                    System.out.println("모달3 listmodalForm: " + listModalForm);
                    listModalForms.add(listModalForm);
                }
                return listModalForms;
            }
        } else if (type.equals("follower")) {
            Optional<User> targetUserById = userRepository.findById(request.getTargetid());
            if (targetUserById.isPresent()) {
                User target = targetUserById.get();
                List<FollowForm> followForms = followRepository.findAllByTouser(target)
                        .stream().map(follow -> modelMapper.map(follow, FollowForm.class)).collect(Collectors.toList());
                for (FollowForm followForm : followForms) {
                    setListModalForms(currentUser, listModalForms, followForm, type);
                }
                return listModalForms;
            }
        } else if (type.equals("following")) {
            Optional<User> targetUserById = userRepository.findById(request.getTargetid());

            if (targetUserById.isPresent()) {
                User target = targetUserById.get();
                List<FollowForm> followForms = followRepository.findAllByFromuser(target)
                        .stream().map(follow -> modelMapper.map(follow, FollowForm.class)).collect(Collectors.toList());
                for (FollowForm followForm : followForms) {

                    setListModalForms(currentUser, listModalForms, followForm, type);

                }

                return listModalForms;
            }
        }
        return null;

    }

    private void setListModalForms(User currentUser, List<ListModalForm> listModalForms, FollowForm followForm, String type) {
        ListModalForm listModalForm = new ListModalForm();
        Boolean isTrue = false;
        User follower = null;
        if (type.equals("follower")) {
            follower = userRepository.findById(followForm.getFromuser().getUserid()).get();
        } else {
            follower = userRepository.findById(followForm.getTouser().getUserid()).get();
        }
        if (currentUser != null) isTrue = followRepository.findByUsers(currentUser, follower).isPresent();

        listModalForm.setUser(modelMapper.map(follower, UserForm.class));
        listModalForm.setIsTrue(isTrue);
        listModalForm.setFollower(followRepository.countByTouser(follower));

        listModalForms.add(listModalForm);
    }

    public List activity(Long current_userid) {
        List<ActivityForm> activityForms = new ArrayList<>();
//        { type: "following", likepostid: 1, username: "공유", alert_date: "2021-10-21" },
//        { type: "follower", commentid: 1, username: "주희", alert_date: "2021-10-21" },
//        { type: "following", likepostid: 1, username: "공유", alert_date: "2021-10-21" },
//        { type: "following", likepostid: 1, username: "공유", alert_date: "2021-10-21" },
//        { type: "follower", commentid: 1, username: "주희", alert_date: "2021-10-21" },
//        { type: "follower", likepostid: 1, username: "주희", alert_date: "2021-10-21" }

//            following: 내가 팔로우한사람의 게시글 (내가 좋아요한 게시글 -> 게시글의 유저가 내가 팔로우한사람인가 ->해당 이슈 리스트 -> 최신순 정렬)

//            follower : 나의 게시물에 좋아요, 댓글(내 게시물을 찾아서 -> 댓글,좋아요 리스트 -> 최신순정렬)
        User currentUser = userRepository.findById(current_userid).get();
        activitySetFollower(currentUser, activityForms);

        //내가 좋아요한 게시글 -> 게시글의 유저가 내가 팔로우한사람인가
        List<LikePost> likes = likePostRepository.findByUser(currentUser).stream().map(likePost -> {
            User targetUser = likePost.getPost().getUser();
            Optional<Follow> byUsers = followRepository.findByUsers(currentUser, targetUser);
            if (byUsers.isPresent()){
                return likePost;
            }else return null;
        }).collect(Collectors.toList());
        likes.removeIf(likePost -> likePost==null);
        System.out.println(likes);
        Collections.sort(likes, (one, other) -> one.getRdate().compareTo(other.getRdate()) == 1 ? -1 : 1);//desc 정렬
        for (LikePost likePost : likes) {
            ActivityForm activityForm = new ActivityForm("following",likePost.getLikepostid(),null,likePost.getPost().getUser().getUsername(),likePost.getRdate(),likePost.getPost().getPostid());
            activityForms.add(activityForm);
        }

        List<Comment> comments=commentRepository.findAllByUser(currentUser).stream().map(comment -> {
            User targetUser = comment.getPost().getUser();
            Optional<Follow> byUsers = followRepository.findByUsers(currentUser, targetUser);
            if (byUsers.isPresent()){
                return comment;
            }else return null;
        }).collect(Collectors.toList());

        comments.removeIf(likePost -> likePost==null);
        System.out.println(comments);
        Collections.sort(comments, (one, other) -> one.getCommentdate().compareTo(other.getCommentdate()) == 1 ? -1 : 1);//desc 정렬
        for (Comment comment : comments) {
            ActivityForm activityForm = new ActivityForm("following",null,comment.getCommentid(),comment.getPost().getUser().getUsername(),comment.getCommentdate(),comment.getPost().getPostid());
            activityForms.add(activityForm);
        }

        Collections.sort(activityForms, (one, other) -> one.getAlert_date().compareTo(other.getAlert_date()) == 1 ? -1 : 1);//desc 정렬


        return activityForms;
    }

    private void activitySetFollower(User currentUser, List<ActivityForm> activityForms) {
        List<Post> posts = postRepository.findAllByUser(currentUser);

        List<Comment> comments = new ArrayList<>();
        List<LikePost> likes = new ArrayList<>();
        List<List> collect;

         collect = posts.stream().map(post -> {
            List<Comment> commentOfPost = commentRepository.findAllByPost(post);
            return commentOfPost;
        }).collect(Collectors.toList());
        for (List commentList : collect) {//각 댓글 종합
            comments.addAll(commentList);
        }
        Collections.sort(comments, (one, other) -> one.getCommentdate().compareTo(other.getCommentdate()) == 1 ? -1 : 1);//desc 정렬
        for (Comment comment : comments) {
            ActivityForm activityForm = new ActivityForm("follower", null, comment.getCommentid(), comment.getUser().getUsername(), comment.getCommentdate(),comment.getPost().getPostid());
            activityForms.add(activityForm);
        }

        collect = posts.stream().map(post -> {
            List<LikePost> likePostsOfPost = likePostRepository.findAllByPost(post);
            return likePostsOfPost;
        }).collect(Collectors.toList());

        for (List likeList : collect) {//각 좋아요 종합
            likes.addAll(likeList);
        }
        Collections.sort(likes, (one, other) -> one.getRdate().compareTo(other.getRdate()) == 1 ? -1 : 1);//desc 정렬
        for (LikePost likePost : likes) {
            ActivityForm activityForm = new ActivityForm("follower",likePost.getLikepostid(), null, likePost.getUser().getUsername(), likePost.getRdate(),likePost.getPost().getPostid());
            activityForms.add(activityForm);
            System.out.println("activityForm = " + activityForm);
        }



    }
}
