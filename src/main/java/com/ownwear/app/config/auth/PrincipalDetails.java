package com.ownwear.app.config.auth;

//시큐리티가 /login 리퀘스트를 낚아채서 로그인 진행 > 로그인 진행 완료시 시큐리티 session을 만들어서 데이터를 넣어준다.(Security ContextHolder)
//오브젝트 -> Authentication(인증) 타입 객체 여야함
//Authentication안에는 User 정보가 있어야함 User -> UserDetails 타입 객체 여야함


import com.ownwear.app.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {


    private User user; //콤포지션

    private Map<String,Object> attributes;


    public PrincipalDetails(User user) {
        this.user = user;
    }

    public PrincipalDetails(User user,Map<String ,Object> attributes) {
        this.user = user;
        this.attributes=attributes;
    }

    //해당 유저의 권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //user.getRole(); 리턴 타입이 안맞음
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {

        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
