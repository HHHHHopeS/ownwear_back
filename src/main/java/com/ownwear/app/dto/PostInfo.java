package com.ownwear.app.dto;

import com.ownwear.app.entity.User;
import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
@Data
public class PostInfo {

    private Long postid;

    private User user;

    private Map<String , Object> imgdata = new HashMap<>();

    private Timestamp rdate;

    private Timestamp edate;

}
