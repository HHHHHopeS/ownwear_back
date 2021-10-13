package com.ownwear.app.dto;

import lombok.Data;

@Data
public class UserInfo {

    private Long userid;

    private String userimg;

    private String username;

    private String email;

    private Boolean sex;

    private String height;

    private String instaid;

    private String pinterestid;

    private String twitterid;

    private Boolean isverified;

    private Boolean ischecked;

    private Boolean isfollowing;
}
