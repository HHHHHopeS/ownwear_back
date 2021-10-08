package com.ownwear.app.form;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ownwear.app.model.Post;
import com.ownwear.app.model.User;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
public class LikepostForm {

    private Long likepost_id;

    private Post post;

    private User user;

}
