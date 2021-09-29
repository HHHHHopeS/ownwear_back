package com.ownwear.app.controller;

import com.ownwear.app.model.Comment;
import com.ownwear.app.model.Post;
import com.ownwear.app.repository.CommentRepository;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/create")
    public Comment createComment(@RequestBody Comment comment) {
        return commentRepository.save(comment);
    }

    @GetMapping("/{post_id}")
    public List<Comment> getComments(@PathVariable("post_id") long post_id) {

        Optional<Post> post = postRepository.findById(post_id);
        commentRepository.findByPost(post.get());

        return commentRepository.findByPost(post.get());

    }

    @PostMapping("/update")
    public List<Comment> saveComment(@RequestBody Comment comment) {

        return null;
    }

    @GetMapping("/delete")
    public List<Comment> deleteComment(@RequestBody Comment comment) {

        return null;

    }
}
