package com.ownwear.app.dto;

import lombok.Data;

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
