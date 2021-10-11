package com.ownwear.app.form;

import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentForm {

    private long commentid;

    private Post post;

    private User user;

    private String content;

    private Timestamp commentdate;
}