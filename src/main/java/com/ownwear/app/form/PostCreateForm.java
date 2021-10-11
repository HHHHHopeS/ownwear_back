package com.ownwear.app.form;

import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PostCreateForm {

        private UserInfo user;

        private Map<String , Object> imgData = new HashMap<>();

        private List<String> hashtags;

        private List<String> brands;

}
