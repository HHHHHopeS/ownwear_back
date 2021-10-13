package com.ownwear.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class IndexRequest {
    String url;
    int page;
    List<Long> ids;
}
