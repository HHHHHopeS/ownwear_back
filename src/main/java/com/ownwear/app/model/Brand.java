package com.ownwear.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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