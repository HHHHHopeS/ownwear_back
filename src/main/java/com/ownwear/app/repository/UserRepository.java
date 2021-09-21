package com.ownwear.app.repository;

import com.ownwear.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//CRUD함수를 jpa에서 들고이따.
//@Repository 가 없어도 jpaRepository가 상속을 해서 안해도됨

public interface UserRepository extends JpaRepository<User,Long> {
    //findBy 규칙  -> Username문법
    //select * from user where username= ?
    Optional<User> findByUsername(String username);//Jpa Query Method + Optional은 null값의 처리를 간단하게 하기위하여 사용

    Optional<User> findByEmail(String email);

    Optional<User> findByProviderId(String providerId);

//    public User findByUseremail(String useremail);
}
