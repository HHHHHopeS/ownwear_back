package com.ownwear.app.form;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserInfo {

    private long user_id;

    private String userimg;

    private String username;

    private String email;

    private boolean sex;

    private String height;

    private String instaid;

    private String pinterestid;

    private String twitterid;

    private Boolean isverified;


}
