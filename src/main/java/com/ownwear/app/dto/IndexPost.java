package com.ownwear.app.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
public class IndexPost {

    private Long postid;

    private UserInfo user;

    private Map<String , Object> imgdata = new HashMap<>();

    private Timestamp rdate;

    private Long likecount;

    private Long commentcount;

}
