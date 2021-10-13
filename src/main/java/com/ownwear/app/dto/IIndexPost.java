package com.ownwear.app.dto;

import com.ownwear.app.entity.User;

import java.sql.Timestamp;
import java.util.Map;

public interface IIndexPost {

    Long getPostid();
    User getUser();
    Map<String, Object> getImgdata();
    Timestamp getRdate();

}
