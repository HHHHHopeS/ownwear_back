package com.ownwear.app.form;

import com.ownwear.app.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
@Data
public class PostInfo {

    private long post_id;

    private User user;

    private Map<String , Object> imgdata = new HashMap<>();

    private Timestamp rdate;

    private Timestamp edate;

}
