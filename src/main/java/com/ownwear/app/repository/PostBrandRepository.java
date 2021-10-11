package com.ownwear.app.repository;

import com.ownwear.app.form.IIndexBrand;
import com.ownwear.app.form.IIndexPost;
import com.ownwear.app.model.Brand;
import com.ownwear.app.model.LikePost;
import com.ownwear.app.model.PostBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostBrandRepository extends JpaRepository<PostBrand, Long> {


    @Query(value = "SELECT Count(p.brand_id) as count, p.brand_id , b.brandname " +
            "FROM Post_brand p " +
            "NATURAL JOIN Brand b " +
            "GROUP BY p.brand_id " +
            "HAVING Count(p.brand_id) >= 1 " +
            "ORDER BY Count(p.brand_id) desc LIMIT 9;",nativeQuery = true)
    List<IIndexBrand> findTop9ByCountByBrand();



    @Query(value = "SELECT post_id, user_id as user , imgData FROM post where post_id IN (SELECT p.post_id " +
            "FROM Post_brand p " +
            "NATURAL JOIN Brand b " +
            "GROUP BY p.brand_id " +
            "HAVING Count(p.brand_id) >= 1 " +
            "ORDER BY Count(p.brand_id) desc ) LIMIT 6;",nativeQuery = true)
    List<IIndexPost> findTop6PostByBrand();
}
