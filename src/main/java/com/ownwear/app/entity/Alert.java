package com.ownwear.app.entity;

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
    @Column(name = "alertid")
    private Long alertid;

    String type;//like follow comment

    @Column(name = "alert_content", nullable = false, length = 500)
    private String alertcontent;

    @Column(name = "ischecked", nullable = false)
    private boolean ischecked;

    @CreationTimestamp
    @Column(name = "alert_date", nullable = false)
    private Timestamp alertdate;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user")
    private User user;
}