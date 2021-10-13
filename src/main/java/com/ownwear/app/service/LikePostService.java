package com.ownwear.app.service;

import com.ownwear.app.entity.LikePost;
import com.ownwear.app.entity.User;
import com.ownwear.app.repository.LikePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikePostService {

    @Autowired
    private LikePostRepository likePostRepository;

    public List<LikePost> checklike(User user) {

        List<LikePost> likePost = likePostRepository.findByUser(user);

        return likePost;
    }

    public boolean likeToggle(LikePost likePost) {

        Optional<LikePost> byId = likePostRepository.findById(likePost.getLikepostid());

        if (byId.isPresent()) {
            likePostRepository.delete(likePost);
            return  false;
        }

        likePostRepository.save(likePost);

        return true;
    }

    public long likecount(LikePost likePost) {

        long countpost = likePostRepository.countByPost(likePost.getPost());

        return countpost;
    }


}
