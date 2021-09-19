package com.ownwear.app.repository;

import com.ownwear.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//CRUD함수를 jpa에서 들고이따.
//@Repository 가 없어도 jpaRepository가 상속을 해서 안해도됨

public interface UserRepository extends JpaRepository<User,Long> {
    //findBy 규칙  -> Username문법
    //select * from user where username= ?
    User findByUsername(String username);//Jpa Query Method

    User findByEmail(String email);

    User findByProviderId(String providerId);

//    public User findByUseremail(String useremail);
}
