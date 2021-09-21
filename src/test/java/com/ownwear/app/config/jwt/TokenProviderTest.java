package com.ownwear.app.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Set;


class TokenProviderTest {

    @Test
    public void test(){
        String sign = JWT.create()
                .withSubject(Long.toString(123L))
                .withClaim("name","안녕")
                .withClaim("email","aa@naver.com")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
//                .withClaim() 굳이 정보 넣을필요 없음
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        System.out.println("##test JWT: "+ sign);
        DecodedJWT decode = JWT.decode(sign);
        System.out.println("##test decode sub: "+decode.getSubject());
        System.out.println("##test decode claims: "+decode.getClaims());
        Set<String> strings = decode.getClaims().keySet();
        for (String s : strings ){
            System.out.println("##clame key :"+ s);
            System.out.println("##clame value : "+ decode.getClaims().get(s).asString());
        }



    }

}