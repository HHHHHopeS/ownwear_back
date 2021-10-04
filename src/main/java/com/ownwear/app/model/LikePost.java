package com.ownwear.app.model;

<<<<<<< Updated upstream
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
=======
import com.fasterxml.jackson.annotation.JsonIgnore;
>>>>>>> Stashed changes
import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikePost {
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likepost_id")
    private Long likepost_id;

<<<<<<< Updated upstream

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
=======
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
>>>>>>> Stashed changes
    @JoinColumn(name = "user_id")
    private User user;

    public LikePost(Post post, User user) {
        this.post = post;
        this.user = user;
    }
}