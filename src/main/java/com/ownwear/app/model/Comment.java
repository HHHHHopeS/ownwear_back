package com.ownwear.app.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentno;
    @Column(name = "POSTNO", nullable = false)
    private int postno;
    @Column(name = "ID", nullable = false)
    private long id;
    @Column(name = "CONTENT", nullable = false)
    private String content;
    @CreationTimestamp
    private Timestamp commentdate;
}
