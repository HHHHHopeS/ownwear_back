package com.ownwear.app.form;

import lombok.Data;

import java.util.List;

@Data
public class IndexRequest {
    String url;
    int page;
    List<Long> ids;
}
