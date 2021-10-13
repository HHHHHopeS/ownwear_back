package com.ownwear.app.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    private User fromuser;


    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "touser")
    private User touser;

    @Builder
    public Follow(User fromuser, User touser){
        this.fromuser=fromuser;
        this.touser=touser;
    }
}