package com.ownwear.app.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CurrentUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long currentusers_id;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public CurrentUsers() {
    }

    public CurrentUsers(long id, String token) {
        this.currentusers_id = id;
        this.token = token;
    }
}