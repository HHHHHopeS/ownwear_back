package com.ownwear.app.dto;

import com.ownwear.app.entity.Post;
import com.ownwear.app.entity.User;
import lombok.Data;

@Data
public class LikePostForm {

    private Long likepostid;

    private Post post;

    private User user;

}
