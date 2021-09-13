package com.ownwear.app.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
public class PostHashTag {
    @Column(name = "POSTNO", nullable = false, unique = true)
    private int postno;
    @Column(name = "HASHTAG", nullable = false, length = 100, unique = true)
    private String hashtag;
}
