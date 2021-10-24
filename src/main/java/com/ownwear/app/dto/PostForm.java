package com.ownwear.app.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data
public class PostForm {

    private Long postid;

    private UserInfo user;

    private Map<String , Object> imgdata = new HashMap<>();

    private List<String> hashtags;

    private List<String> brands;

    private Timestamp rdate;

    private Timestamp edate;

}
