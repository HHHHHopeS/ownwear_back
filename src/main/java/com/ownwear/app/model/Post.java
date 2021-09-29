package com.ownwear.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private long post_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "imgdata", nullable = false, length = 4000)
    private String imgdata;

    @CreationTimestamp
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Timestamp rdate;

    @UpdateTimestamp
    private Timestamp edate;

    @OneToMany(mappedBy = "post")
    private List<LikePost> likePost;

    @OneToMany(mappedBy = "post")
    private List<PostHashTag> posthashtag;

    @OneToMany(mappedBy = "post")
    private List<Comment> comment;
}
