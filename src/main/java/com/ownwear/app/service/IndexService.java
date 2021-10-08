package com.ownwear.app.service;

import com.ownwear.app.model.Brand;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.BrandRepository;
import com.ownwear.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BrandRepository brandRepository;

    public List<User> SrchUserData(String username) {

        List<User> byUsernameStartsWith = userRepository.findByUsernameStartsWith(username);

        List<Brand> byBrandnameStartsWith = brandRepository.findByBrandnameStartsWith(username);



        if (byUsernameStartsWith.isEmpty()) {
            return null;
        }

        if (byBrandnameStartsWith.isEmpty()) {
            return null;
        }

        return byUsernameStartsWith;
    }
}
