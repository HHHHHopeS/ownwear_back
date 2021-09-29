package com.ownwear.app.security;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            System.out.println("##필터");
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromToken(jwt);
                System.out.println("##필터2");
                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                System.out.println("##필터2");
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                System.out.println("##필터2");
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                System.out.println("##필터2");


                SecurityContextHolder.getContext().setAuthentication(authentication); //스프링 시큐리티 에서 컨텍스트에 어선티케이션 강제 저장
                System.out.println("##필터2");
                Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
                System.out.println("##필터2");
                System.out.println(authentication1.toString());
                System.out.println(authentication1.getAuthorities().toString());
                UserPrincipal userPrincipal = (UserPrincipal)authentication1.getPrincipal();
                System.out.println("##필터2");
                System.out.println(userPrincipal.getEmail());
            }

        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        System.out.println("##필터3");
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
