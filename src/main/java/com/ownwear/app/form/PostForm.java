package com.ownwear.app.form;

import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
@Data
public class PostForm {

    private Long post_id;

    private UserInfo user;

    private Map<String , Object> imgdata = new HashMap<>();

    private Timestamp rdate;

    private Timestamp edate;

}
