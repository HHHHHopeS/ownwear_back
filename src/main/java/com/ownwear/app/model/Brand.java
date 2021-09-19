package com.ownwear.app.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Data
public class Brand {
    @Column(name = "BRAND", nullable = false, unique = true)
    private String brand;
}
