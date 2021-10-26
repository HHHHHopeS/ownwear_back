package com.ownwear.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityForm {
    private String type;
    private Long likepostid;
    private Long commentid;
    private String username;
    private Timestamp alert_date;
    private Long postid;
}
