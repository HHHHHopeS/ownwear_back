package com.ownwear.app.config.oauth;

import com.ownwear.app.config.auth.PrincipalDetails;
import com.ownwear.app.exception.ResourceNotFoundException;
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
import java.util.Optional;

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
        System.out.println("##AccessToken: " + userRequest.getAccessToken());
        System.out.println("##TokenValue: " + userRequest.getAccessToken().getTokenValue());
        System.out.println("##getClientRegistration: " + userRequest.getClientRegistration()); //registrationId 로  어떤 OAuth로 로그인햇는지 확인 가능.
        //
        OAuth2User oauth2User = super.loadUser(userRequest);

        System.out.println("##getAttribute: " + oauth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId(); //facebook, ClientRegistration: ClientRegistration{registrationId='facebook', clientId='603118660853659', clientSecret='ed5da6393cd3c33bfff415fe07603961', clientAuthenticationMethod=org.springframework.security.oauth2.core.ClientAuthenticationMethod@86baaa5b, authorizationGrantType=org.springframework.security.oauth2.core.AuthorizationGrantType@5da5e9f3, redirectUri='{baseUrl}/{action}/oauth2/code/{registrationId}', scopes=[email, public_profile], providerDetails=org.springframework.security.oauth2.client.registration.ClientRegistration$ProviderDetails@25b20c29, clientName='Facebook'}
        String providerId = provider + "_" + oauth2User.getAttribute("id");
        String providerEmail = oauth2User.getAttribute("email");
        String password = passwordEncoder.encode("asd");
        String role = "ROLE_USER";
        String username = providerId;
        System.out.println("##email : " + providerEmail + ", username : " + username);
//        String username = oauth2User.

        Optional userEntityOp = userRepository.findByProviderId(providerId);
        User userEntity=null;
        if (userEntityOp.isPresent()) {
            userEntity = userRepository.findByProviderId(providerId).get();
        }
        System.out.println("##PrincipalOAuth2UserService fineByProviderId user:" + userEntity);
        if (userEntity == null) {
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(providerEmail)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
             userRepository.save(userEntity);
//            SecurityContextHolder
        }
        return new PrincipalDetails(userEntity, oauth2User.getAttributes()); //todo 자동회원가입시 마이페이지 수정
    }
/*
    private Map<String, Object> getUserAttributes(ResponseEntity<Map<String, Object>> response) {
        Map<String, Object> userAttributes = response.getBody();
        if (userAttributes.containsKey("response")) {
            LinkedHashMap responseData = (LinkedHashMap) userAttributes.get("response");
            userAttributes.putAll(responseData);
            userAttributes.remove("response");
        }
        return userAttributes;
    }*/
}
