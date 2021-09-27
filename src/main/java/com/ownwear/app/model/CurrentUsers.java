package com.ownwear.app.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class CurrentUsers {

    @Id
    private long id;

    private String token;

    public CurrentUsers() {
    }

    public CurrentUsers(long id, String token) {
        this.id = id;
        this.token = token;
    }
}

