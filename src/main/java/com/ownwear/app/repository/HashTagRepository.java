package com.ownwear.app.repository;

import com.ownwear.app.model.Comment;
import com.ownwear.app.model.HashTag;
import com.ownwear.app.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    Optional<HashTag> findById(long hashTag_id);

    List<HashTag> findByHashtagnameStartsWith(String brandname);
}
