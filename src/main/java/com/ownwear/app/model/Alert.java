package com.ownwear.app.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alert {
    @Id
    @GeneratedValue
    @Column(name = "Alert_id")
    private long id;

    @Column(name = "ALERTCONTENT", nullable = false, length = 500)
    private String alertcontent;

    @Column(name = "ALERTCHECK", nullable = false)
    private boolean alertcheck;

    @CreationTimestamp
    @Column(name = "ALERTDATE", nullable = false)
    private Timestamp alertdate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}