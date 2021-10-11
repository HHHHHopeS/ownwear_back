package com.ownwear.app.repository;

import com.ownwear.app.form.IIndexPost;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(long post_id);

    @Query("select p.post_id as post_id , p.user as user , p.imgdata as imgdata from Post p where p.post_id=?1")
    Optional<IIndexPost> findInterfaceById(long post_id);

    List<Post> findByUser(User user); //해당 유저의 게시글

    Page<Post> findAll(Pageable pageable);//모든 게시글

//    Page<Post> findAllByUserIn(List<User> user, Pageable pageable);

    Page<Post> findAllByUser(User users, Pageable pageable); //유저의 컬럼에 따른 모든 게시글

    void deleteById(long id);

    @Query("select p from Post p where p.user = ?1")
    List<Post> findAllByUser(User user); //유저 디테일 페이지

    List<IIndexPost> findTop6ByOrderByRdateDesc();

    List<IIndexPost> findTop6ByOrderByRdateAsc();


    @Query("select Max(p.post_id) from Post p ")
    Long maxById();

}
