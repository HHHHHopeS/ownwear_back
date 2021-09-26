package com.ownwear.app.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userImg;
    private String username;
    private String password;
    private String email;
    private String role;
    @CreationTimestamp
    private Timestamp rdate;

    //facebook
    private String provider;
    private String providerId;
    private boolean isVerified=true;

    private String sex;
    private String height;
    private String instaId;
    private String pinterestId;
    private String twitterId;

    @Builder
    public User(String username, String password, String email, String role, Timestamp rdate, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.rdate = rdate;
        this.provider = provider;
        this.providerId= providerId;
        this.isVerified = false;
    }

    public User() {
        this.setRole("ROLE_USER");
    }
}
