package com.ownwear.app.security;

import com.ownwear.app.config.AppProperties;
import com.ownwear.app.entity.CurrentUsers;
import com.ownwear.app.entity.User;
import com.ownwear.app.repository.CurrentUsersRepository;
import com.ownwear.app.repository.UserRepository;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class TokenProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CurrentUsersRepository currentUsersRepository;

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
        String token = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();

        //System.out.println("##tokenprovider userPrincipal Id : "+userPrincipal.getId());
        User user = userRepository.findById(userPrincipal.getId()).get();
        CurrentUsers currentUsers = new CurrentUsers(user, token);
        Optional<CurrentUsers> byUser = currentUsersRepository.findByUser(user);
        if (byUser.isPresent()){
            currentUsersRepository.deleteById(byUser.get().getCurrentusersid());
        }
        currentUsersRepository.save(currentUsers);
        return token;
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

}
