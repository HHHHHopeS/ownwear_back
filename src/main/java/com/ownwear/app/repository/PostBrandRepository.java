package com.ownwear.app.repository;

import com.ownwear.app.dto.IIndexBrand;
import com.ownwear.app.entity.PostBrand;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostBrandRepository extends JpaRepository<PostBrand, Long> {

    @Query(value = "SELECT Count(p.brandid) as counts, p.brandid , b.brandname " +
            "FROM post_brand p " +
            "NATURAL JOIN brand b " +
            "GROUP BY p.brandid " +
            "HAVING Count(p.brandid) >= 1 " +
            "ORDER BY Count(p.brandid) desc LIMIT 9;",nativeQuery = true)
    List<IIndexBrand> findTop9ByCountByBrand();

    @Query(value = "SELECT p.postid " +
            "FROM post_brand p " +
            "NATURAL JOIN brand b " +
            "GROUP BY p.brandid " +
            "HAVING Count(p.brandid) >= 1 " +
            "ORDER BY Count(p.brandid) desc  LIMIT 6;",nativeQuery = true)
    List<Long> findTop6PostidByBrand();

    @Query(value = "SELECT p.post.postid " +
            "FROM PostBrand p " +
            "JOIN Brand b " +
            "ON p.brand.brandid = b.brandid and p.post.user.sex = ?1 " +
            "GROUP BY p.brand.brandid, p.post_brandid " +
            "HAVING Count(p.brand.brandid) >= 1 " +
            "ORDER BY Count(p.brand.brandid) desc")
    List<Long> findTop6PostidByBrand(Boolean sex);




    //, po.imgData as imgData
}
