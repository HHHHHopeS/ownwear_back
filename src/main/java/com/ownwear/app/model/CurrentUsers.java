package com.ownwear.app.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class CurrentUsers {

    @Id
    @Column(name = "userno")
    private long userid;

    private String token;

    public CurrentUsers() {
    }

    public CurrentUsers(long userid, String token) {
        this.userid = userid;
        this.token = token;
    }
}

