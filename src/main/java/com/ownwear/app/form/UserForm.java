package com.ownwear.app.form;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserForm {

    private Long user_id;

    private String userimg;

    private String username;

    private String email;

    private String role;

    private Timestamp rdate;

    //facebook
    private String provider;

    private String providerid;

    private boolean isverified = true;

    private boolean sex;

    private String height;

    private String instaid;

    private String pinterestid;

    private String twitterid;


}
