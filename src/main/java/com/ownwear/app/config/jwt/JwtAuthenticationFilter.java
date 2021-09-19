package com.ownwear.app.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ownwear.app.config.auth.PrincipalDetails;
import com.ownwear.app.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
/*
* /login 요청해서 username, password 전송하면 (Post)
*UsernamePasswordAuthenticationFilter 동작함
* 따라서 config 에 다시 등록해줘야함
* */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
    // 인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("##JwtAuthenticationFilter: 로그인 시도 중");
        /* 1. username 과 password  받기
        *  2. 정상인지 로그인 시도 하기 (authenticationManager 로 로그인 시도를하면 ?
        *  3. PrincipalDetailsService 가 호출-> loadUserByUsername
        *  4. -> PrincipalDetails 세션에 담고(권한 관리를 위해해
        *  5. JWT토큰을 만들어서 응답해주면 됨 */


        ObjectMapper om = new ObjectMapper();
        LoginDto loginDto = null;
        try {
            /*

            System.out.println(request.getInputStream());
            BufferedReader br = request.getReader();

            String input = null;
            while ((input  = br.readLine())!=null){
                System.out.println(input);
            }*/
            loginDto = om.readValue(request.getInputStream(),LoginDto.class);
            System.out.println("##JwtAuthenticationFilter loginDto: "+loginDto);


            // 유저네임패스워드 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

            System.out.println("##JwtAuthenticationFilter 토큰생성완료 ");


            //PrincipalDetailService 의 loadByUsername() 호출
            Authentication authentication =
                    authenticationManager.authenticate(authenticationToken);

            // authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
            // loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
            // UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
            // UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
            // Authentication 객체를 만들어서 필터체인으로 리턴해준다.

            // Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
            // Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
            // 결론은 인증 프로바이더에게 알려줄 필요가 없음.

            //authentication 객체가 session영역에 저장됨. => 로그인 됨
            //리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는거임.
            //굳이 JWT토큰을 사용하면서 세션을 만들이유가 없음 근데 단지 권한처리때문에 세션에 넣어주는것
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername());

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("=============================");

        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        System.out.println("썪쎼스");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        System.out.println("##썩쎼스 principalDetails: "+principalDetails);

        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("id",principalDetails.getUser().getId())
                .withClaim("username",principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));


        response.addHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX+jwtToken);
        super.successfulAuthentication(request, response, chain, authResult);


    }

}
