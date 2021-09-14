package com.ownwear.app.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
public class Post {
    @Id
    @Column(name = "POSTNO")
    private int postno;
    private long id;
    @Column(name = "IMGDATA", nullable = false, length = 4000)
    private String imgdata;
    @CreationTimestamp
    @Column(name = "RDATE", nullable = false)
    private Timestamp rdate;
    private Timestamp edate;

    @OneToMany
    @JoinColumn(name = "POSTNO")
    private Set<LikePost> likePosts;
    private Set<Comment> comments;
    private Set<PostHashTag> postHashTags;
}
