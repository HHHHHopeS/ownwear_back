package com.ownwear.app.service;

import com.ownwear.app.form.BrandForm;
import com.ownwear.app.form.HashTagForm;
import com.ownwear.app.form.SearchForm;
import com.ownwear.app.form.UserForm;
import com.ownwear.app.model.Brand;
import com.ownwear.app.model.HashTag;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.BrandRepository;
import com.ownwear.app.repository.HashTagRepository;
import com.ownwear.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IndexService {

    final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private HashTagRepository hashTagRepository;

    public SearchForm SrchUserData(String keyword) {

        List<User> byUsernameStartsWith = userRepository.findByUsernameStartsWith(keyword);

        List<UserForm> userForms = new ArrayList<>();

        for (User user : byUsernameStartsWith) {
            UserForm userForm = modelMapper.map(user, UserForm.class);
            userForms.add(userForm);
        }

        List<Brand> byBrandnameStartsWith = brandRepository.findByBrandnameStartsWith(keyword);

        List<BrandForm> brandForms = new ArrayList<>();

        for (Brand brand : byBrandnameStartsWith) {
            BrandForm map = modelMapper.map(brand, BrandForm.class);
            brandForms.add(map);
        }

        List<HashTag> byHashtagnameStartsWith = hashTagRepository.findByHashtagnameStartsWith(keyword);

        List<HashTagForm> hashTagForms = new ArrayList<>();

        for (HashTag hashTag : byHashtagnameStartsWith) {
            HashTagForm map = modelMapper.map(hashTag, HashTagForm.class);
            hashTagForms.add(map);
        }

        SearchForm searchForm = new SearchForm();

        searchForm.setUserForms(userForms);
        searchForm.setBrandForms(brandForms);
        searchForm.setHashTagForms(hashTagForms);

        return searchForm;
    }
}
