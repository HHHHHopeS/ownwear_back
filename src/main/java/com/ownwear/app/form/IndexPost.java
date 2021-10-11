package com.ownwear.app.form;

import com.ownwear.app.model.User;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class IndexPost {

    private Long post_id;

    private UserInfo user;

    private Map<String , Object> imgData = new HashMap<>();
}
