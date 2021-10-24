package com.ownwear.app.service;

import com.ownwear.app.dto.LikePostForm;
import com.ownwear.app.dto.LikeRequest;
import com.ownwear.app.entity.LikePost;
import com.ownwear.app.entity.Post;
import com.ownwear.app.entity.User;
import com.ownwear.app.repository.LikePostRepository;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LikePostService {

    private LikePostRepository likePostRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

    public List<LikePost> checklike(User user) {

        List<LikePost> likePost = likePostRepository.findByUser(user);

        return likePost;
    }

    public boolean likeToggle(LikeRequest likeRequest) {

        User user = null;
        Post post = null;

        Optional<User> userOPt = userRepository.findById(likeRequest.getUserid());
        Optional<Post> postOpt = postRepository.findById(likeRequest.getPostid());
        Optional<LikePost> likePostOp= null;
        if (postOpt.isPresent()&& userOPt.isPresent()) {
            user = userOPt.get();
            post = postOpt.get();
            likePostOp = likePostRepository.findByUserAndPost(user, post);
        }
        if (likePostOp.isPresent()) {
            likePostRepository.delete(likePostOp.get());
            return  false;
        }else {
            LikePost likePost = new LikePost(post,user);
            likePostRepository.save(likePost);
            return true;
        }
    }

    public long likecount(LikePost likePost) {

        long countpost = likePostRepository.countByPost(likePost.getPost());

        return countpost;
    }


}
