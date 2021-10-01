package com.ownwear.app.model;

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
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class HashTag {

    @Id
    @Column(name = "hashtag_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String hashtag;

    @Column(nullable = false, unique = true)
    private String hashtagname;

    @JsonManagedReference
    @OneToMany(mappedBy = "hashtag")
    private List<PostHashTag> postHashTag = new ArrayList<>();
}
