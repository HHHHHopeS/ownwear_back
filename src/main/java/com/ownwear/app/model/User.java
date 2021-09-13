package com.ownwear.app.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
public class User {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String useremail;
    private String role;
    @CreationTimestamp
    private Timestamp rdate;
    private String sex;
    private String height;

    private String instaId;
    private String pinterestId;
    private String twitterId;

    @OneToMany
    @JoinColumn(name = "ID")
    private Set<Alert> alerts;
    private Set<Post> posts;
    private Set<LikePost> likePosts;
    private Set<Comment> comments;
    private Set<Follow> follows;
}
//aeijfoawejfoawejfoawejf