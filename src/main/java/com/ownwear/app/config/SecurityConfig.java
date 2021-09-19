package com.ownwear.app.config;

import com.ownwear.app.config.jwt.JwtAuthenticationFilter;
import com.ownwear.app.config.jwt.JwtAuthorizationFilter;
import com.ownwear.app.config.oauth.OAuth2SuccessHandler;
import com.ownwear.app.config.oauth.PrincipalOAuth2UserService;
import com.ownwear.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.web.filter.CorsFilter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ownwear.app.config.oauth.SocialType.FACEBOOK;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOAuth2UserService principalOAuth2UserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OAuth2SuccessHandler successHandler;

    private final CorsFilter corsFilter;
//cjvbfwfwyg_1631086512@tfbnw.net   100059341094395
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.addFilterBefore(new MyFilter1(), SecurityContextPersistenceFilter.class); //시큐리티 컨피규어 이전에 실행
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))//필수 파라미터 AuthenticationManager
                .addFilter(new JwtAuthorizationFilter(authenticationManager(),userRepository))//필수 파라미터 AuthenticationManager
                .addFilter(corsFilter)
                .authorizeRequests().antMatchers("/", "/test/**","/oauth2/**","/join/**","/joinform/**", "/login/**", "/loginform/**","/replace/**","/css/**",
                "/images/**", "/js/**", "/console/**", "/favicon.ico/**","/accounts/login").permitAll()
                .antMatchers("/facebook").hasAuthority(FACEBOOK.getRoleType())
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().authenticated()
//            .and()
//                .formLogin()
//                .loginProcessingUrl("/login")
            .and()
                .oauth2Login()
                .successHandler(successHandler)
                .userInfoEndpoint().userService(principalOAuth2UserService);

    }

//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")//스프링 시큐리티에 있는 user라는 개념이 가지고있는 가장 핵심적인 속성들 (다른 속성 많음)
//                        .password("password")
//                        .roles("USER")
//                        .build();
//        return new InMemoryUserDetailsManager(user);
//    }

    //패스워드 인코더 빈 등록(어디서든 autowired 하면 사용가능)
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
/*

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(
            OAuth2ClientProperties oAuth2ClientProperties
           */
/* @Value("${custom.oauth2.kakao.client-id}") String kakaoClientId,
            @Value("${custom.oauth2.kakao.client-secret}") String kakaoClientSecret,
            @Value("${custom.oauth2.naver.client-id}") String naverClientId,
            @Value("${custom.oauth2.naver.client-secret}") String naverClientSecret*//*
) {
        List<ClientRegistration> registrations = oAuth2ClientProperties
                .getRegistration().keySet().stream()
                .map(client -> getRegistration(oAuth2ClientProperties, client))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

      */
/*  registrations.add(CustomOAuth2Provider.KAKAO.getBuilder("kakao")
                .clientId(kakaoClientId)
                .clientSecret(kakaoClientSecret)
                .jwkSetUri("temp")
                .build());

        registrations.add(CustomOAuth2Provider.NAVER.getBuilder("naver")
                .clientId(naverClientId)
                .clientSecret(naverClientSecret)
                .jwkSetUri("temp")
                .build());*//*

        return new InMemoryClientRegistrationRepository(registrations);
    }


    private ClientRegistration getRegistration(OAuth2ClientProperties clientProperties, String client) {
       */
/* if("google".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("google");
            return CommonOAuth2Provider.GOOGLE.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .scope("email", "profile")
                    .build();
        }*//*

        if("facebook".equals(client)) {
            OAuth2ClientProperties.Registration registration = clientProperties.getRegistration().get("facebook");
            return CommonOAuth2Provider.FACEBOOK.getBuilder(client)
                    .clientId(registration.getClientId())
                    .clientSecret(registration.getClientSecret())
                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,link")
                    .scope("email","public_profile")
                    .build();
        }

        return null;
    }
}
*/
