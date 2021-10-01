package com.ownwear.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long user_id;

    private String userimg;

    private String username;

    private String password;

    private String email;

    private String role;

    @CreationTimestamp
    private Timestamp rdate;

    //facebook
    private String provider;

    private String providerid;

    private boolean isverified = true;

    private boolean sex;

    private String height;

    private String instaid;

    private String pinterestid;

    private String twitterid;

    @Builder
    public User(String username, String password, String email, String role, Timestamp rdate, String provider, String providerid) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.rdate = rdate;
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


    @OneToMany(mappedBy = "user")
    private List<Alert> alerts = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    
    @OneToMany(mappedBy = "user")
    private List<LikePost> likePosts = new ArrayList<>();

    
    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    
    @OneToMany(mappedBy = "to")
    private List<Follow> followers;

    
    @OneToMany(mappedBy = "from")
    private List<Follow> following;

}
