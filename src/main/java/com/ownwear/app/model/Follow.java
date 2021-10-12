package com.ownwear.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Follow", uniqueConstraints = {@UniqueConstraint(
        name = "Followid_UNIQUE",
        columnNames = {"fromuser", "touser"}
)})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "followid")
    private Long followid;


    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "fromuser")
    private User from;


    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "touser")
    private User to;
}