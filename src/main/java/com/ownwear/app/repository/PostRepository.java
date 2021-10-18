package com.ownwear.app.repository;

import com.ownwear.app.dto.IIndexPost;
import com.ownwear.app.entity.Post;
import com.ownwear.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(long postid);

    @Query("select p from Post p where p.postid=?1")
    Optional<IIndexPost> findInterfaceById(long postid);

    List<Post> findByUser(User user); //해당 유저의 게시글

    Page<Post> findAll(Pageable pageable);//모든 게시글

//    Page<Post> findAllByUserIn(List<User> user, Pageable pageable);

    Page<Post> findAllByUser(User users, Pageable pageable); //유저의 컬럼에 따른 모든 게시글

    void deleteById(long id);

    //    @Query("select p from Post p where p.user = ?1")
    List<IIndexPost> findAllByUser(User user); //유저 디테일 페이지

    List<IIndexPost> findTop6ByOrderByRdateDesc();

    List<IIndexPost> findTop6ByOrderByRdateAsc();

    @Query("select Max(p.postid) from Post p ")
    Long maxById();

    //    @Query("SELECT p FROM Post p where p.postid In :longs")
    List<IIndexPost> findAllByPostidIn(List<Long> longs);

    String findRandkingDataQ =
            "SELECT p " +
            "FROM Post p " +
            "JOIN (" +
                    "SELECT l.post.postid, count(l.post) as likecount " +
                    "FROM LikePost l " +
                    "GROUP BY l.post " +
                    "ORDER BY likecount DESC) as lp " +
            "ON lp.l.post.postid = p.postid";

    @Query("select p  from Post p join LikePost l on l.post.postid  = p.postid group by l.post.postid order by count(l.post.postid) desc")
    Page<Post> findRankingData(String filter, Pageable pageRequest); //좋아요 랭킹
    /*SELECT * FROM Post p  JOIN (SELECT l.postid as postids, COUNT(l.postid) as likecount FROM Like_Post l GROUP BY l.postid ORDER BY likecount DESC) as lp ON lp.postids = p.postid*/

}
