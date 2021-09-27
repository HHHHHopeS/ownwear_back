package com.ownwear.app.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class LikePost {
    @Id //todo 좋아요 테이블에 프라이머리키가 있으면 안됨
    private long postno;
    @Column(name = "ID", nullable = false)
    private long id;
}