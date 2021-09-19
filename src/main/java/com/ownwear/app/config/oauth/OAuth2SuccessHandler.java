package com.ownwear.app.config.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ownwear.app.config.auth.PrincipalDetails;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
        String providerId = user.getProviderId();
        System.out.println("##onAuthenticationSuccess providerId: "+providerId);
        User userEntity  = userRepository.findByProviderId(providerId);
        String jwtToken="";
        if (userEntity == null) { //최초 로그인시
             jwtToken = JWT.create()
                    .withSubject("hopes토큰")
                    .withExpiresAt(new Date(System.currentTimeMillis() + (60000) * (10)))
                    .withClaim("id", user.getId())
                    .withClaim("provider", user.getProvider())
                    .withClaim("providerId", user.getProviderId())
                    .withClaim("email", user.getEmail())
                    .withClaim("username", user.getUsername())
                    .sign(Algorithm.HMAC512("hopes"));
            response.addHeader("Authorization","Bearer "+jwtToken);
            System.out.println("##최초로그인: "+jwtToken);
        }else{
            jwtToken = JWT.create()
                    .withSubject("hopes토큰")
                    .withExpiresAt(new Date(System.currentTimeMillis() + (60000) * (10)))
                    .withClaim("id", userEntity.getId())
                    .withClaim("username", userEntity.getUsername())
                    .sign(Algorithm.HMAC512("hopes"));
            response.addHeader("Authorization","Bearer "+jwtToken);
        }
    }
}
