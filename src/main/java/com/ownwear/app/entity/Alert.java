package com.ownwear.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alert {


    /*
          alertid:1,
          type: "like",
          post_no: "1",
          username: "카리나",
          userimg: exPhoto,
          date: "1900-10-5 13:01:00"
    */
    @Id
    @GeneratedValue
    @Column(name = "alertid")
    private Long alertid;

   //todo like follow comment
    @Column(name = "type", nullable = false, length = 100)
    private String type;


    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postid")
    private Post post;

    @Column(name = "ischecked", nullable = false)
    private boolean ischecked;

    @CreationTimestamp
    @Column(name = "alert_date", nullable = false)
    private Timestamp alertdate;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "fromuser")
    private User fromuser;


    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "touser")
    private User touser;
}