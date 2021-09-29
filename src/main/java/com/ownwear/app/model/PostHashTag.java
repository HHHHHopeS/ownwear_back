package com.ownwear.app.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostHashTag {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posthashtag_id")
    private long posthashtag_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private HashTag hashtag;
}
