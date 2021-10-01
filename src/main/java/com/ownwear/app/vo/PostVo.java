package com.ownwear.app.vo;

import com.ownwear.app.model.Comment;
import com.ownwear.app.model.HashTag;
import com.ownwear.app.model.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostVo {
    private Post post;
    private int likecount;
    private ArrayList<HashTag> hashtags;
    private ArrayList<Post> userRelated;
    private ArrayList<Comment> comments;
    private String username;

}
