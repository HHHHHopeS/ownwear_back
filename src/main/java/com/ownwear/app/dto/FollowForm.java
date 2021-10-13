package com.ownwear.app.dto;

import com.ownwear.app.entity.User;
import lombok.Data;

@Data
public class FollowForm {
    private Long followid;
    private User touser;
    private User fromuser;
}
