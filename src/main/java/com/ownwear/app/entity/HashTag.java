package com.ownwear.app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HashTag {

    @Id
    @Column(name = "hashtagid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hashtagid;

    @Column(nullable = false, unique = true)
    private String hashtagname;

    @JsonBackReference
    @OneToMany(mappedBy = "hashtag")
    private List<PostHashTag> postHashTag = new ArrayList<>();

    @Builder
    public HashTag(String hashtagname){
        this.hashtagname = hashtagname;
    }
}
