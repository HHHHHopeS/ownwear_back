package com.ownwear.app.repository;

import com.ownwear.app.dto.IIndexUser;
import com.ownwear.app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username); // UserDetail 페이지

    Optional<User> findById(Long id); //유저 My page

    Boolean existsByEmail(String email);

    List<User> findAllBySex(boolean sex); // 성별로 유저 search

    @Query(value = "SELECT Count(f.touser) as count, u.userid , u.username, u.userimg " +
            "FROM follow f " +
            "Join user u ON u.userid = f.touser " +
            "GROUP BY f.touser " +
            "HAVING Count(f.touser) >= 1 " +
            "ORDER BY Count(f.touser) desc LIMIT 7;", nativeQuery = true
    )
    List<IIndexUser> findTop7ByFollow();

    List<User> findByUsernameStartsWith(String username);


    @Modifying
    @Transactional
    @Query("update User  set height=:height,instaid=:instaid,twitterid=:twitterid,pinterestid=:pinterestid,userimg=:userimg where userid=:userid")
    void updateUser(long userid,String height,String instaid,String twitterid,String pinterestid,String userimg);

    @Modifying
    @Transactional
    @Query("update User set password=:password where userid=:userid")
    void updatePw(long userid,String password);

    @Query("select u  from User u join Follow f on f.touser.userid  = u.userid group by f.touser.userid order by count(f.touser.userid) desc, u.rdate desc")
    Page<User> findRankingData( Pageable pageRequest);

    @Query("select u  from User u join Follow f on f.touser.userid  = u.userid and u.sex = :sex group by f.touser.userid order by count(f.touser.userid) desc, u.rdate desc")
    Page<User> findRankingData(Boolean sex, Pageable pageRequest);

}
