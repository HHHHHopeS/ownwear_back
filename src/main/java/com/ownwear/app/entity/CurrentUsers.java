package com.ownwear.app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CurrentUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long currentusersid;

    private String token;


    @JsonManagedReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User user;

    public CurrentUsers() {
    }

    public CurrentUsers(User user, String token) {
        this.user = user;
        this.token = token;
    }
}