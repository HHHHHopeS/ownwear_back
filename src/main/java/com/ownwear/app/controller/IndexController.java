package com.ownwear.app.controller;

import com.ownwear.app.dto.IndexForm;
import com.ownwear.app.dto.IndexPost;
import com.ownwear.app.dto.IndexRequest;
import com.ownwear.app.dto.SearchForm;
import com.ownwear.app.service.IndexService;
import lombok.AllArgsConstructor;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("index")
public class IndexController {

    private IndexService indexService;

    @GetMapping("/getindex")
    public IndexForm getIndex(String url){
        return indexService.getIndex(url);
    }

    @PostMapping("/getindex")
    public Map<String, List<IndexPost>> getIndexScroll(@RequestBody IndexRequest indexRequest){
        return indexService.getIndexScroll(indexRequest);
    }

    @GetMapping("/srchdata")
    public SearchForm SrchData(String value ,String keyword) {

        SearchForm searchForm = indexService.SrchUserData(value,keyword);

        return searchForm;
    }

    @GetMapping("AutoComplete/{type}") //todo 구성하기
    private List<Object> hashtagAutoComplete(String data,@PathVariable("type") String type){

        return indexService.autoComplete(data,type);
    }

    @GetMapping("ranking")
    public List<Object> getRanking(String type , String filter , int page){
//        return indexService.getRanking(type,filter,page);
        return null;
    }

}