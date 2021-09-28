package com.ownwear.app.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Follow", uniqueConstraints = {@UniqueConstraint(
        name = "Follow_id_UNIQUE",
        columnNames = {"FLWD_ID", "FLWN_ID"}
)})
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_user")
    @Column(name = "from_user", nullable = false)
    private User from;

    @ManyToOne
    @JoinColumn(name = "to_user")
    @Column(name = "to_user", nullable = false)
    private User to;
}
