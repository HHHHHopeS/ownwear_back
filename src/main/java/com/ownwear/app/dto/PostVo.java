package com.ownwear.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostVo {
    private PostForm postform;
    private long likecount;
    private List<HashTagForm> hashtags;
    private List<PostForm> userRelated;
    private List<CommentForm> comments;
    private String username;

}
