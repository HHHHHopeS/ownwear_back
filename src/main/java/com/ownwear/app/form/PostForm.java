package com.ownwear.app.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ownwear.app.model.Comment;
import com.ownwear.app.model.LikePost;
import com.ownwear.app.model.PostHashTag;
import com.ownwear.app.model.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data
public class PostForm {

    private long post_id;

    private UserInfo user;

    private Map<String , Object> imgdata = new HashMap<>();

    private Timestamp rdate;

    private Timestamp edate;

}
