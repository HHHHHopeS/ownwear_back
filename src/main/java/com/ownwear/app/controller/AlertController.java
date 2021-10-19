package com.ownwear.app.controller;

import com.ownwear.app.dto.CommentForm;
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

    @GetMapping("/{postid}")
    public List<CommentForm> getComments(@PathVariable("postid") long postid) {
        return commentService.getComments(postid);
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
