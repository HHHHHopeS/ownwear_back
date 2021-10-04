package com.ownwear.app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PostBrand {

    @Id
    @GeneratedValue
    @Column(name = "post_brand_id")
    private long post_brand_id;

//    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
//    @Column(name = "brand_id")
    private Brand brand;

//    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
