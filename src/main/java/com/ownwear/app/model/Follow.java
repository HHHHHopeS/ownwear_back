package com.ownwear.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Follow", uniqueConstraints = {@UniqueConstraint(
        name = "Follow_id_UNIQUE",
        columnNames = {"from_user", "to_user"}
)})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private long follow_id;

    
    @ManyToOne
    @JoinColumn(name = "from_user")
    private User from;

    
    @ManyToOne
    @JoinColumn(name = "to_user")
    private User to;
}