package com.ownwear.app.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
public class HashTag {
    @Id
    private String hashtag;

    @OneToMany
    @JoinColumn(name = "HASHTAG")
    private Set<PostHashTag> postHashTags;
}
