package com.ownwear.app.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "ALERTCONTENT", nullable = false, length = 500)
    private String alertcontent;
    @Column(name = "ALERTCHECK", nullable = false)
    private boolean alertcheck;
    @CreationTimestamp
    @Column(name = "ALERTDATE", nullable = false)
    private Timestamp alertdate;
}