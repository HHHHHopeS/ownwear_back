package com.ownwear.app.repository;

import com.ownwear.app.model.HashTag;
import com.ownwear.app.model.LikePost;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;


public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {

    ArrayList<HashTag> findByPost(Post post);

}
