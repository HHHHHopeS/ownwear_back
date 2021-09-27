package com.ownwear.app.vo;

import com.ownwear.app.model.Comment;
import com.ownwear.app.model.HashTag;
import com.ownwear.app.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostVo {
    private Post postData;
    private int likecount;
    private ArrayList<HashTag> hashtags;
    private ArrayList<UserRelatedVo> userRelated;
    private ArrayList<Comment> comments;
    private String username;

}
