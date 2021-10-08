package com.ownwear.app.form;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class IndexForm {

    private Map<String , List<PostForm>> postForms = new HashMap<>();

    private List<IndexUser> userInfos;

    private List<IndexBrand> brandForms;

    private List<IndexHashTag> hashTagForms; //hashtag name으로

}