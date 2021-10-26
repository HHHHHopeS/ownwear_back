package com.ownwear.app.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ActivityForm {
    private String type;
    private Long likepostid;
    private Long commentid;
    private String username;
    private Timestamp alert_date;
}
