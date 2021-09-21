package com.ownwear.app.config.jwt;

public interface JwtProperties {
    String SECRET = "hopes"; // 우리 서버만 알고 있는 비밀값
    int EXPIRATION_TIME = 60000*30; // 1분*10 (1/1000초)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization"; //리스폰스 헤더 네임
}