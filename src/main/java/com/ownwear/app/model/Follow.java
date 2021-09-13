package com.ownwear.app.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Data
@Table(name = "Follow", uniqueConstraints = {@UniqueConstraint(
        name = "Follow_id_UNIQUE",
        columnNames = {"FLWD_ID", "FLWN_ID"}
)})
public class Follow {
    private long flwd_id;
    private long flwn_id;
}
