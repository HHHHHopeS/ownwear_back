package com.ownwear.app.config.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ownwear.app.config.AppProperties;
import com.ownwear.app.config.auth.PrincipalDetails;
import com.ownwear.app.config.jwt.TokenProvider;
import com.ownwear.app.exception.BadRequestException;
import com.ownwear.app.exception.ResourceNotFoundException;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Optional;

import static com.ownwear.app.config.oauth.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private TokenProvider tokenProvider;

    private AppProperties appProperties;

    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    OAuth2SuccessHandler(TokenProvider tokenProvider, AppProperties appProperties,
                                       HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.tokenProvider = tokenProvider;
        this.appProperties = appProperties;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /*PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
        String providerId = user.getProviderId();
        System.out.println("##onAuthenticationSuccess providerId: "+providerId);
            User userEntity  = userRepository.findByProviderId(providerId).orElseThrow(() -> new ResourceNotFoundException("User", "providerId", providerId)
            );
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
        request.getSession().setAttribute("token",jwtToken);
        response.sendRedirect("/replace");*/
        String targetUrl = determineTargetUrl(request, response, authentication);
        System.out.println("##targetUrl : "+ targetUrl);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());


        String token = tokenProvider.createToken(authentication);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
