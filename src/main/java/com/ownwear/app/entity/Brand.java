package com.ownwear.app.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand {

    @Id
    @Column(name = "brandid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandid;

    private String brandname;

    public Brand(String brandname){
        this.brandname = brandname;
    }
}