package com.ownwear.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
