package com.ownwear.app.controller;

import com.ownwear.app.form.IndexForm;
import com.ownwear.app.form.IndexPost;
import com.ownwear.app.form.IndexRequest;
import com.ownwear.app.form.SearchForm;
import com.ownwear.app.model.User;
import com.ownwear.app.service.IndexService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class IndexController {

    @Autowired
    IndexService indexService;

    @GetMapping("/getindex")
    public IndexForm getIndex(){
        System.out.println("getIndex 입장");
        return indexService.getIndex();
    }

    @PostMapping("/getindex")
    public Map<String, List<IndexPost>> getIndexScroll(@RequestBody IndexRequest indexRequest){
        System.out.println(indexRequest);
        return indexService.getIndexScroll(indexRequest);
    }

    @GetMapping("/srchdata")
    public SearchForm SrchData(String keyword) {

        SearchForm searchForm = indexService.SrchUserData(keyword);

        return searchForm;
    }

    @RequestMapping("/hashtagAutoComplete") //todo 구성하기
    private static JSONArray hashtagAutoComplete(@RequestBody String data){
        String[] str = {"assdf","asdfwefewf","asfwefew"};

        JSONArray arr = JSONArray.fromObject(str);
        return arr;
    }
}