package com.ownwear.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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

    public CurrentUsers(User user, String token) {
        this.user = user;
        this.token = token;
    }
}