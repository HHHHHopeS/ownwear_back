package com.ownwear.app.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand {

    @Id
    @Column(name = "brand_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String brand_id;

    private String brandname;
}