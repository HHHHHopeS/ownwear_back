package com.ownwear.app.service;

import com.ownwear.app.dto.*;
import com.ownwear.app.entity.*;
import com.ownwear.app.repository.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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


    public SearchForm SrchUserData(String value, String keyword) {
        SearchForm searchForm = new SearchForm();
        switch (keyword) {
            case "user":
                List<User> byUsernameStartsWith = userRepository.findByUsernameStartsWith(value);

                List<UserForm> userForms = new ArrayList<>();

                for (User user : byUsernameStartsWith) {
                    UserForm userForm = modelMapper.map(user, UserForm.class);
                    userForms.add(userForm);
                }
                searchForm.setUserForms(userForms);
                break;
            case "brand":
                List<Brand> byBrandnameStartsWith = brandRepository.findByBrandnameStartsWith(value);

                List<BrandForm> brandForms = new ArrayList<>();

                for (Brand brand : byBrandnameStartsWith) {
                    BrandForm map = modelMapper.map(brand, BrandForm.class);
                    brandForms.add(map);
                }
                searchForm.setBrandForms(brandForms);
                break;
            case "tag":
                List<HashTag> byHashtagnameStartsWith = hashTagRepository.findByHashtagnameStartsWith(value);

                List<HashTagForm> hashTagForms = new ArrayList<>();

                for (HashTag hashTag : byHashtagnameStartsWith) {
                    HashTagForm map = modelMapper.map(hashTag, HashTagForm.class);
                    hashTagForms.add(map);
                }
                searchForm.setHashTagForms(hashTagForms);
                break;
            default:
                return null;
        }


        return searchForm;
    }


    public IndexForm getIndex(String url) {
        // Rank , Sex , Tag , Brand , 최신
        IndexForm indexForm = new IndexForm();
        for (String s : Arrays.asList("rank", "new")) {
            indexForm.setPostMap(mapSetting(s, indexForm.getPostMap(), null, url)); //랭크기준 포스트 리스트 담기
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
            postMap = mapSetting("brand", new HashMap<>(), null, indexRequest.getUrl()); //랭크기준 포스트 리스트 담기
        } else if (page >= 2) {
            postMap = mapSetting("random", new HashMap<>(), indexRequest.getIds(), indexRequest.getUrl());

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
            indexBrand.setTagCount(iIndexBrand.getCounts());
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

    private Map<String, List<IndexPost>> mapSetting(String topic, Map<String, List<IndexPost>> map, List<Long> ids, String url) {
        List<IndexPost> postFormList = new ArrayList<>();
        Boolean sex = null;
        if (url == null || url.equals("all")) {
            sex = null;
        } else {
            url = url.trim();
            sex = url.equals("men");
        }
        switch (topic) {
            case "rank":
                if (sex == null) {
                    postFormList = likePostRepository.findTop6ByPost()
                            .stream().map(post -> modelMapper.map(post, IndexPost.class)).collect(Collectors.toList());
                    if (postFormList.size() > 6) postFormList = postFormList.subList(0, 6);
                } else {
                    postFormList = likePostRepository.findTop6ByPost(sex)
                            .stream().map(post -> modelMapper.map(post, IndexPost.class)).collect(Collectors.toList());
                    if (postFormList.size() > 6) postFormList = postFormList.subList(0, 6);

                }
                break;
            case "new":
                if (sex == null) {
                    List<IIndexPost> newPosts = postRepository.findAllInterface();
                    if (newPosts.size() > 6) newPosts = newPosts.subList(0, 6);
                    postFormList = getIndexPost(newPosts, postFormList);
                } else {
                    List<IIndexPost> newPosts = postRepository.findAllInterface(sex);
                    if (newPosts.size() > 6) newPosts = newPosts.subList(0, 6);
                    postFormList = getIndexPost(newPosts, postFormList);
                }
                break;
            case "brand":
                if (sex == null) {
                    Long brandid = postBrandRepository.findTopBrandid();
                    List<IIndexPost> top6PostidByBrand = postBrandRepository.findTop6PostidByBrand(brandid).stream().map(postid -> {
                        return postRepository.findInterfaceById(postid).get();
                    }).collect(Collectors.toList());
                    if (top6PostidByBrand.size() > 6) top6PostidByBrand = top6PostidByBrand.subList(0,6);
                    postFormList = getIndexPost(top6PostidByBrand, postFormList);
                } else {
                    Long brandid = postBrandRepository.findTopBrandid();
                    List<IIndexPost> top6PostidByBrand = postBrandRepository.findTop6PostidByBrand(brandid).stream().map(postid -> {
                        return postRepository.findInterfaceById(postid).get();
                    }).collect(Collectors.toList());
                    final boolean check = sex;
                    top6PostidByBrand.removeIf(iIndexPost -> (postRepository.findById(iIndexPost.getPostid()).get().getUser().getSex()!=check));
                    if (top6PostidByBrand.size() > 6) top6PostidByBrand = top6PostidByBrand.subList(0,6);
                    postFormList = getIndexPost(top6PostidByBrand, postFormList);

                }
                break;
            case "random":
                long totalCount = postRepository.maxById() + 1;
                Set<Long> randoms = new HashSet<>();
                List<IIndexPost> iIndexPosts = new ArrayList<>();
                long random = 0;
                boolean check = true;
                if (ids.isEmpty()) {
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
            indexPost.setUser(modelMapper.map(newPost.getUser(), UserInfo.class));
            postFormList.add(indexPost);
        }

        return postFormList;
    }


    public List autoComplete(String data, String type) {
        List list = null;
        switch (type) {
            case "hashtag":
                list = hashTagRepository.findByHashtagnameStartsWith(data)
                        .stream().map(hashTag -> modelMapper.map(hashTag, HashTagForm.class)).collect(Collectors.toList());
                break;
            case "usertag":
                list = userRepository.findByUsernameStartsWith(data)
                        .stream().map(user -> modelMapper.map(user, UserForm.class)).collect(Collectors.toList());
                break;
            default:
                return list;
        }
        if (list.size() > 4) list = list.subList(0, 4);
        return list;

    }
}
