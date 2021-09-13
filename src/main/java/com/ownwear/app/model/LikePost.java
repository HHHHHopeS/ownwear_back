package com.ownwear.app.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class LikePost {
    @Id
    private int postno;
    @Column(name = "ID", nullable = false)
    private long id;
}
