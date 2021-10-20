package com.ownwear.app.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BrandInfo {
    private String brandname;
    private long postcount;
    private List posts = new ArrayList();
//     brandname:,
//     postedcount: 브랜드 태깅한 포스트 카운트,
//     [post]: 태깅한 포스트 단 3개 순서는 최신순이던 인기순이던 상관 무,
//     }
}
