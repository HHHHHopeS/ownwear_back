package com.ownwear.app.service;

import com.ownwear.app.form.*;
import com.ownwear.app.model.Brand;
import com.ownwear.app.model.HashTag;
import com.ownwear.app.model.LikePost;
import com.ownwear.app.model.User;
import com.ownwear.app.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class IndexService {

    final ModelMapper modelMapper = new ModelMapper();

    private UserRepository userRepository;
    private BrandRepository brandRepository;
    private HashTagRepository hashTagRepository;
    private PostRepository postRepository;
    private LikePostRepository likePostRepository;
    private PostBrandRepository postBrandRepository;
    private PostHashTagRepository postHashTagRepository;


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


    public IndexForm getIndex() {
        // Rank , Sex , Tag , Brand , 최신
        IndexForm indexForm = new IndexForm();
        for (String s : Arrays.asList("rank", "new")) {
            indexForm.setPostMap(mapSetting(s, indexForm.getPostMap(), null)); //랭크기준 포스트 리스트 담기
        }//최신기준 포스트 리스트 담기
        indexForm.setUserInfos(getIndexUsers());
        indexForm.setHashTagForms(getPostHashTagInfos());
        indexForm.setBrandForms(getIndexBrands());
//        indexForm.setUserInfos(userRepository.findTop7ByFollow());
//                private List<UserInfo> userInfos;

        return indexForm;
    }

    public Map<String, List<IndexPost>> getIndexScroll(IndexRequest indexRequest) {

        int page = indexRequest.getPage();
        // Rank , Sex , Tag , Brand , 최신

        Map<String, List<IndexPost>> postMap = null;

        if (page == 1) {
            postMap = mapSetting("brand", new HashMap<>(), null); //랭크기준 포스트 리스트 담기
        } else if (page >= 2) {
            postMap = mapSetting("random", new HashMap<>(), indexRequest.getIds());

        }
        return postMap;
    }
    /*
     * tag
     * brand
     * new
     * recommend(random)
     *
     *
     * */


    private List<IndexUser> getIndexUsers() {
        List<IIndexUser> iIndexUsers = userRepository.findTop7ByFollow();
        List<IndexUser> indexUsers = new ArrayList<>();
        for (IIndexUser iIndexUser : iIndexUsers) {
            IndexUser indexUser = new IndexUser();
            indexUser.setFollow(iIndexUser.getCount());
            indexUser.setUserid(iIndexUser.getUserid());
            indexUser.setUsername(iIndexUser.getUserName());
            indexUser.setUserimg(iIndexUser.getUserImg());
            indexUsers.add(indexUser);
        }
        return indexUsers;
    }

    private List<IndexBrand> getIndexBrands() {
        List<IIndexBrand> iIndexBrands = postBrandRepository.findTop9ByCountByBrand();
        List<IndexBrand> indexBrands = new ArrayList<>();
        for (IIndexBrand iIndexBrand : iIndexBrands) {
            IndexBrand indexBrand = new IndexBrand();
            indexBrand.setTagCount(iIndexBrand.getCount());
            indexBrand.setBrandid(iIndexBrand.getBrandid());
            indexBrand.setBrandName(iIndexBrand.getBrandName());
            indexBrands.add(indexBrand);
        }
        return indexBrands;
    }

    private List<IndexHashTag> getPostHashTagInfos() {

        List<IIndexHashTag> iIndexHashTags = postHashTagRepository.findTop9ByCountByHashtagInterface();
        List<IndexHashTag> indexHashTags = new ArrayList<>();
        for (IIndexHashTag iIndexHashTag : iIndexHashTags) {
            IndexHashTag indexHashTag = new IndexHashTag();
            indexHashTag.setCount(iIndexHashTag.getCount());
            indexHashTag.setHashtagid(iIndexHashTag.getHashtagid());
            indexHashTag.setHashtagName(iIndexHashTag.getHashtagName());
            indexHashTags.add(indexHashTag);
        }
        return indexHashTags;
    }

    private Map<String, List<IndexPost>> mapSetting(String topic, Map<String, List<IndexPost>> map, List<Long> ids) {
        List<IndexPost> postFormList = new ArrayList<>();
        switch (topic) {
            case "rank":
                List<LikePost> top6ByPost = likePostRepository.findTop6ByPost();
                for (LikePost likePost : top6ByPost) {
                    LikepostForm likepostForm = modelMapper.map(likePost, LikepostForm.class);
                    IndexPost indexPost = modelMapper.map(likepostForm.getPost(), IndexPost.class);
                    postFormList.add(indexPost);
                }
                break;
            case "new":
                List<IIndexPost> newPosts = postRepository.findTop6ByOrderByRdateAsc();
                postFormList = getIndexPost(newPosts, postFormList);
                break;
            case "brand":
                List<Long> top6PostidByBrand = postBrandRepository.findTop6PostidByBrand();
                List<IIndexPost> brandPosts = postRepository.findAllByPostidIn(top6PostidByBrand);
                postFormList = getIndexPost(brandPosts, postFormList);
                break;
            case "random":
                System.out.println("Random 입장");
                long totalCount = postRepository.maxById() + 1;
                Set<Long> randoms = new HashSet<>();
                List<IIndexPost> iIndexPosts = new ArrayList<>();
                long random = 0;
                boolean check = true;
                if (ids.isEmpty() ) {
                    while (check) {
                        random = ThreadLocalRandom.current().nextLong(totalCount);
                        if (!randoms.contains(random)) {
                            Optional<IIndexPost> interfaceById = postRepository.findInterfaceById(random);
                            if (interfaceById.isPresent()) {
                                IIndexPost iIndexPost = interfaceById.get();
                                iIndexPosts.add(iIndexPost);
                                randoms.add(random);
                            }
                        }
                        check = randoms.size() < 6 && randoms.size() < totalCount;
                    }
                } else {
                    while (randoms.size() < 6 && ids.size() + randoms.size() < totalCount) {
                        while (check) {
                            check = false;
                            random = ThreadLocalRandom.current().nextLong(totalCount);
                            for (Long aLong : ids) {
                                if (aLong == random) {
                                    check = true;
                                }
                            }
                        }
                        if (!randoms.contains(random)) {
                            Optional<IIndexPost> interfaceById = postRepository.findInterfaceById(random);
                            if (interfaceById.isPresent()) {
                                IIndexPost iIndexPost = interfaceById.get();
                                iIndexPosts.add(iIndexPost);
                                randoms.add(random);
                            }
                        }
                        check = randoms.size() < 6 && ids.size() + randoms.size() < totalCount;
                    }
                }
                postFormList = getIndexPost(iIndexPosts, postFormList);
                break;
        }

        map.put(topic, postFormList);
        return map;
    }


    private List<IndexPost> getIndexPost(List<IIndexPost> newPosts, List<IndexPost> postFormList) {

        for (IIndexPost newPost : newPosts) {
            IndexPost indexPost = new IndexPost();
            indexPost.setPostid(newPost.getPostid());
            indexPost.setImgdata(newPost.getImgdata());
            indexPost.setUser( modelMapper.map(newPost.getUser(), UserInfo.class));
            postFormList.add(indexPost);
        }

        return postFormList;
    }

}
