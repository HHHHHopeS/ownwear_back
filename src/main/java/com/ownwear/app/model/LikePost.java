package com.ownwear.app.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class LikePost {

    @Id //todo 좋아요 테이블에 프라이머리키가 있으면 안됨
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likepost_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @Column(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Column(name = "user_id", nullable = false)
    private User user;

}