package com.ownwear.app.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentForm {

    private long commentid;

    private PostForm post;

    private UserForm user;

    private String content;

    private Timestamp commentdate;
}