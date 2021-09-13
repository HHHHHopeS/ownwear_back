package com.ownwear.app.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class Follow {
    @Column(name = "FLWD_ID", nullable = false, unique = true)
    private long flwd_id;
    @Column(name = "FLWN_ID", nullable = false, unique = true)
    private long flwn_id;
}
