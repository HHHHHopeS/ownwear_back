package com.ownwear.app.form;

import com.ownwear.app.model.User;

import java.sql.Timestamp;
import java.util.Map;

public interface IIndexPost {

    Long getPostid();
    User getUser();
    Map<String, Object> getImgdata();
    Timestamp getRdate();

}
