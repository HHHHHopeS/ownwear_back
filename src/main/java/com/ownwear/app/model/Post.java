package com.ownwear.app.model;

import com.fasterxml.jackson.annotation.*;
import com.ownwear.app.form.PostForm;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@TypeDef(
        name = "json",
        typeClass = JsonStringType.class
)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private long post_id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "IMGDATA", columnDefinition = "json")
    @Type(type = "json")
    private Map<String , Object> imgdata = new HashMap<>();

    @CreationTimestamp
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Timestamp rdate;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Timestamp edate;

    @JsonBackReference
    @OneToMany(mappedBy = "post")
    private List<LikePost> likePost;


    @JsonBackReference
    @OneToMany(mappedBy = "post")
    private List<PostHashTag> posthashtag;


    @JsonBackReference
    @OneToMany(mappedBy = "post")
    private List<Comment> comment;


}
