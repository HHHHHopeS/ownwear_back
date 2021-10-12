package com.ownwear.app.form;

import com.ownwear.app.model.User;
import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
public class IndexPost {

    private Long postid;

    private UserInfo user;

    private Map<String , Object> imgData = new HashMap<>();

    private Timestamp rdate;

    private Long likecount;

    private Long commentcount;

}
