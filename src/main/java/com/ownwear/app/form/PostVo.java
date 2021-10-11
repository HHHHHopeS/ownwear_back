package com.ownwear.app.form;

import com.ownwear.app.form.PostForm;
import com.ownwear.app.model.Comment;
import com.ownwear.app.model.HashTag;
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
    private List<HashTag> hashtags;
    private List<PostForm> userRelated;
    private List<Comment> comments;
    private String username;

}
