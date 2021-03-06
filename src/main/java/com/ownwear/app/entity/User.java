package com.ownwear.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userid;

    private String userimg;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private String role;

    @CreationTimestamp
    private Timestamp rdate;

    //facebook
    private String provider;

    private String providerid;

    private boolean isverified = true;

    private Boolean sex;

    private String height;

    private String instaid;

    private String pinterestid;

    private String twitterid;


    @Builder
    public User(String username, String password, String email, String role, String provider, String providerid) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerid = providerid;
        this.isverified = false;
    }

    public User() {
        this.setRole("ROLE_USER");
    }

    public User(boolean sex) {
        setSex(sex);
    }

//
//    @JsonBackReference
//    @OneToMany(mappedBy = "fromuser")
//    private List<Alert> alerts = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    private List<LikePost> likePosts = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "touser")
    private List<Follow> followers;

    @JsonBackReference
    @OneToMany(mappedBy = "fromuser")
    private List<Follow> following;
}
