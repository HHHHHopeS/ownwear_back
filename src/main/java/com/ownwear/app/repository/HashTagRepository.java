package com.ownwear.app.repository;

import com.ownwear.app.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    Optional<HashTag> findById(long hashTagid);

    List<HashTag> findByHashtagnameStartsWith(String brandname);
    Optional<HashTag> findByHashtagname(String hashtagString);
}
