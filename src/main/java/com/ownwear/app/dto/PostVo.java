package com.ownwear.app.dto;

import com.ownwear.app.model.Comment;
import com.ownwear.app.model.HashTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostVo {
    private long postno;
    private long id;
    private String imgdata;
    private Timestamp rdate;
    private Timestamp edate;
    private int likecount;
    private ArrayList<HashTag> hashtags;
    private ArrayList<UserRelatedVo> userRelated;
    private ArrayList<Comment> comments;

}
