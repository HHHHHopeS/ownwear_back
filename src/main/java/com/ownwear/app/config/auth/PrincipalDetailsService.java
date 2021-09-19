package com.ownwear.app.config.auth;

import com.ownwear.app.config.auth.PrincipalDetails;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//시큐리티에서 loginProcessUrl("/login")
//login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어있는 loadByUsername 함수 호출
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override //로그인시 입력한 username으로 찾음 -> user -> Authentication -> session
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("##PrincipalDetailsService 진입");
        System.out.println("##PrincipalDetailsService load : " + username);
        User user = userRepository.findByUsername(username); //스프링 시큐리티에서 리턴값을 User로 변환할수 없기떄문에 변환
        System.out.println("##PrincipalDetailsService 유저 : "+user);
        if (user != null) {
            return new PrincipalDetails(user);
        }
        return new PrincipalDetails(user);
    }

/*//(3)
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(User.getEmail(), User.getPassword(), authorities);//User는 userDetails 를 implements 중이라 가능
        */



    //(2)
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));//패스워드 인코딩
        return userRepository.save(user);
    }
}
