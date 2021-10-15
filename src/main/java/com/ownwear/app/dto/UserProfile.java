package com.ownwear.app.dto;

import lombok.Data;

@Data
public class UserProfile {

    private  UserInfo user;

    private long follower;

    private long following;
}
