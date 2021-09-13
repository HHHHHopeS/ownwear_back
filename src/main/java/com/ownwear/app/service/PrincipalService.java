package com.ownwear.app.service;

import com.ownwear.app.config.auth.PrincipalDetails;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//시큐리티에서 loginProcessUrl("/login")
//login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어있는 loadByUsername 함수 호출
@Service
public class PrincipalService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override //로그인시 입력한 username으로 찾음 -> user -> Authentication -> session
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("load : "+ username);
        User user = userRepository.findByUsername(username); //스프링 시큐리티에서 리턴값을 User로 변환할수 없기떄문에 변환
        if (user != null){
            return new PrincipalDetails(user);
        }
        //UserDetails 내부
        UserDetails userDetails = new UserDetails() {//임의로 오버라이딩
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                return authorities;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() { //TODO 유저네임 (우리는 이메일로) 은 유일해야한다.
                return user.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() { //만료 안됨?
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {// 안잠김?
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() { // 30일마다 비밀번호 변경 등
                return true;
            }

            @Override
            public boolean isEnabled() {//비활성화
                return true;
            }
        };
        return userDetails;

/*//(3)
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(User.getEmail(), User.getPassword(), authorities);//User는 userDetails 를 implements 중이라 가능
        */

    }

    //(2)
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));//패스워드 인코딩
        return userRepository.save(user);
    }
}
