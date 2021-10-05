package com.ownwear.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Long alert_id;

    @Column(name = "ALERTCONTENT", nullable = false, length = 500)
    private String alertcontent;

    @Column(name = "ALERTCHECK", nullable = false)
    private Boolean alertcheck;

    @CreationTimestamp
    @Column(name = "ALERTDATE", nullable = false)
    private Timestamp alertdate;


    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
}