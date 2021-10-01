package com.ownwear.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Brand {

    @Id
    @Column(name = "brand_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String brand_id;
    private String brandname;
}