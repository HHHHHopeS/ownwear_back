package com.ownwear.app.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class IndexForm {

    private Map<String , List<IndexPost>> postMap = new HashMap<>();

    private List<IndexUser> userInfos;

    private List<IndexBrand> brandForms;

    private List<IndexHashTag> hashTagForms; //hashtag name으로

}
