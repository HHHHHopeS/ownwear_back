package com.ownwear.app.dto;

import lombok.Data;

@Data
public class LikeRequest {
    private long postid;
    private long userid;
}
