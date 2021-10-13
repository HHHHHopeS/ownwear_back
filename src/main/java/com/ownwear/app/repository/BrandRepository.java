package com.ownwear.app.repository;

import com.ownwear.app.dto.IIndexBrand;
import com.ownwear.app.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {


    @Query(value = "SELECT Count(p.brandid) as count, p.brandid , b.brandname " +
            "FROM Post_brand p " +
            "NATURAL JOIN Brand b " +
            "GROUP BY p.brandid " +
            "HAVING Count(p.brandid) >= 1 " +
            "ORDER BY Count(p.brandid) desc LIMIT 9;",nativeQuery = true)
    List<IIndexBrand> findTop9ByCountByBrand();

    List<Brand> findByBrandnameStartsWith(String brandname);

    Optional<Brand> findByBrandname(String brandString);
}
