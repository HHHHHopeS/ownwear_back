package com.ownwear.app.dto;

import lombok.Data;

@Data
public class ListModalRequest {
        private Long current_userid;
        private Long targetid;
        private String type;
}
