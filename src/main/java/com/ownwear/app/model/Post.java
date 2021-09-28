package com.ownwear.app.model;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.json.JSONObject;
import org.json.JSONString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Entity
@Data
@TypeDef(
        name = "json",
        typeClass = JsonStringType.class
)
public class Post {
    @Id
    @Column(name = "POSTNO")
    private long postno;
    private long id;

    @Column(name = "IMGDATA", columnDefinition = "json",nullable = true)
    @Type(type = "json")
    private Map<String , Object> imgdata = new HashMap<>();
    @CreationTimestamp
    @Column(name = "RDATE", nullable = false)
    private Timestamp rdate;
    private Timestamp edate;
//
//    @OneToMany
//    @JoinColumn(name = "POSTNO")
//    private Set<LikePost> likePosts;
//    private Set<Comment> comments;
//    private Set<PostHashTag> postHashTags;
}
