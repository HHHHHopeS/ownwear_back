package com.ownwear.app.security.oauth2;

import com.ownwear.app.dto.UserForm;
import com.ownwear.app.exception.OAuth2AuthenticationProcessingException;
import com.ownwear.app.entity.User;
import com.ownwear.app.repository.UserRepository;
import com.ownwear.app.security.UserPrincipal;
import com.ownwear.app.security.oauth2.user.OAuth2UserInfo;
import com.ownwear.app.security.oauth2.user.OAuth2UserInfoFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
       
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) { //???????????? ????????? ??? ?????? ?????????
            user = userOptional.get();
            String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
            //System.out.println(user);
            if(!user.getProvider().equals(registrationId)) {
//                //System.out.println("???????????? ????????? ??????????????? ??????");
                /*throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");*/
                user.setProvider(registrationId);
//                user.setUsername();
                user.setProviderid(oAuth2UserInfo.getId());
                user.setUserimg(oAuth2UserInfo.getImageUrl());
                userRepository.save(user);
            }
        } else { //????????? ????????????
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        String providerid = oAuth2UserInfo.getId();
        user.setProvider(provider);
        user.setProviderid(providerid);
        String username = provider+"_"+providerid;
//        //System.out.println("    ###CustomOAuth2UserService  registerNewUser : "  + username);
        user.setUsername(username);
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setUserimg(oAuth2UserInfo.getImageUrl());
        user.setPassword(passwordEncoder.encode("hopes123"));
        user.setIsverified(false);
        return userRepository.save(user);
    }



}
