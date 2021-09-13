package com.ownwear.app.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class Brand {
    @Column(name = "BRAND", nullable = false, unique = true)
    private String brand;
}
