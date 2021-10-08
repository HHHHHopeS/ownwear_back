package com.ownwear.app.repository;

import com.ownwear.app.form.IIndexBrand;
import com.ownwear.app.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.ownwear.app.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {


    @Query(value = "SELECT Count(p.brand_id) as count, p.brand_id , b.brandname " +
            "FROM Post_brand p " +
            "NATURAL JOIN Brand b " +
            "GROUP BY p.brand_id " +
            "HAVING Count(p.brand_id) >= 1 " +
            "ORDER BY Count(p.brand_id) desc LIMIT 9;",nativeQuery = true)
    List<IIndexBrand> findTop9ByCountByBrand();
    List<Brand> findByBrandnameStartsWith(String brandname);
}
