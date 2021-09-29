package com.ownwear.app.model;

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
    @Column(name = "hashtag_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String hashtag;

    @Column(nullable = false, unique = true)
    private String hashtagname;

    @OneToMany(mappedBy = "hashtag")
    private List<PostHashTag> postHashTag = new ArrayList<>();
}
