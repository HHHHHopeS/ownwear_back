package com.ownwear.app.config.oauth;

import com.ownwear.app.config.auth.PrincipalDetails;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {
    private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";
    private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";
    private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";
    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE = new ParameterizedTypeReference<Map<String, Object>>() {
    };
    private Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();
    private RestOperations restOperations;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public PrincipalOAuth2UserService() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        this.restOperations = restTemplate;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("##PrincipalOAuth2UserService 입장");
        System.out.println("##AccessToken: "+userRequest.getAccessToken());
        System.out.println("##TokenValue: "+userRequest.getAccessToken().getTokenValue());
        System.out.println("##getClientRegistration: "+userRequest.getClientRegistration()); //registrationId 로  어떤 OAuth로 로그인햇는지 확인 가능.
        //
        OAuth2User oauth2User = super.loadUser(userRequest);

        System.out.println("##getAttribute: "+oauth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId(); //facebook, ClientRegistration: ClientRegistration{registrationId='facebook', clientId='603118660853659', clientSecret='ed5da6393cd3c33bfff415fe07603961', clientAuthenticationMethod=org.springframework.security.oauth2.core.ClientAuthenticationMethod@86baaa5b, authorizationGrantType=org.springframework.security.oauth2.core.AuthorizationGrantType@5da5e9f3, redirectUri='{baseUrl}/{action}/oauth2/code/{registrationId}', scopes=[email, public_profile], providerDetails=org.springframework.security.oauth2.client.registration.ClientRegistration$ProviderDetails@25b20c29, clientName='Facebook'}
        String providerId  = provider+"_"+oauth2User.getAttribute("id");
        String providerEmail = oauth2User.getAttribute("email");
        String password = passwordEncoder.encode("asd");
        String role = "ROLE_USER";
        String username= providerId;
        System.out.println("##email : "+providerEmail+", username : "+username);
//        String username = oauth2User.

        User userEntity =  userRepository.findByProviderId(providerId);
        System.out.println("##PrincipalOAuth2UserService fineByProviderId user:"+userEntity);
        if (userEntity==null){
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(providerEmail)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
//             userRepository.save(userEntity);
//            SecurityContextHolder
        }
        return new PrincipalDetails(userEntity,oauth2User.getAttributes()); //자동회원가입시 마이페이지 수정

        /*Assert.notNull(userRequest, "userRequest cannot be null");
        if (!StringUtils.hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
            OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_INFO_URI_ERROR_CODE, "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        if (!StringUtils.hasText(userNameAttributeName)) {
            OAuth2Error oauth2Error = new OAuth2Error(MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE, "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        RequestEntity<?> request = this.requestEntityConverter.convert(userRequest);
        ResponseEntity<Map<String, Object>> response;
        try {
            response = this.restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
        } catch (OAuth2AuthorizationException ex) {
            OAuth2Error oauth2Error = ex.getError();
            StringBuilder errorDetails = new StringBuilder();
            errorDetails.append("Error details: [");
            errorDetails.append("UserInfo Uri: ").append(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
            errorDetails.append(", Error Code: ").append(oauth2Error.getErrorCode());
            if (oauth2Error.getDescription() != null) {
                errorDetails.append(", Error Description: ").append(oauth2Error.getDescription());
            }
            errorDetails.append("]");
            oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE, "An error occurred while attempting to retrieve the UserInfo Resource: " + errorDetails.toString(), null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
        } catch (RestClientException ex) {
            OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE, "An error occurred while attempting to retrieve the UserInfo Resource: " + ex.getMessage(), null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), ex);
        }
        Map<String, Object> userAttributes = getUserAttributes(response);
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new OAuth2UserAuthority(userAttributes));
        OAuth2AccessToken token = userRequest.getAccessToken();
        for (String authority : token.getScopes()) {
            authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
        }
        return new DefaultOAuth2User(authorities, userAttributes, userNameAttributeName);*/
    } // 네이버는 HTTP response body에 response 안에 id 값을 포함한 유저정보를 넣어주므로 유저정보를 빼내기 위한 작업을 함

    private Map<String, Object> getUserAttributes(ResponseEntity<Map<String, Object>> response) {
        Map<String, Object> userAttributes = response.getBody();
        if (userAttributes.containsKey("response")) {
            LinkedHashMap responseData = (LinkedHashMap) userAttributes.get("response");
            userAttributes.putAll(responseData);
            userAttributes.remove("response");
        }
        return userAttributes;
    }
}