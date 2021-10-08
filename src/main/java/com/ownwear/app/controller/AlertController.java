package com.ownwear.app.controller;

import com.ownwear.app.form.CommentForm;
import com.ownwear.app.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alert")
public class AlertController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public List<CommentForm> createComment(@RequestBody CommentForm comment) {

        return commentService.create(comment);
    }

    @GetMapping("/{post_id}")
    public List<CommentForm> getComments(@PathVariable("post_id") long post_id) {

        return commentService.getComments(post_id);

    }

    @PostMapping("/update")
    public List<CommentForm> saveComment(@RequestBody CommentForm comment) {
        return commentService.update(comment);

    }

    @GetMapping("/delete")
    public List<CommentForm> deleteComment(@RequestBody CommentForm comment) {
        return commentService.delete(comment);

    }
}
