package com.ownwear.app.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Search {

    @Id
    @Column(name = "srch_id", nullable = false, length = 100)
    private String srch_id;

    @Column(name = "srchcategory", nullable = false, length = 100)
    private String srchcategory;

    @Column(name = "srchname", nullable = false, length = 100)
    private String srchname;

    @CreationTimestamp
    @Column(name = "srchdate", nullable = false)
    private Timestamp srchdate;
}
