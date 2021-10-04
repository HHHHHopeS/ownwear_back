package com.ownwear.app.service;

import com.ownwear.app.form.CommentForm;
import com.ownwear.app.model.Comment;
import com.ownwear.app.model.Post;
import com.ownwear.app.repository.CommentRepository;
import com.ownwear.app.repository.PostRepository;
import com.ownwear.app.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    final ModelMapper modelMapper = new ModelMapper();

    public List<CommentForm> create(CommentForm commentForm) {
        Optional<Post> postById = postRepository.findById(commentForm.getPost().getPost_id()); //해당 게시글 존재여부
        if (postById.isPresent()) {
            Comment comment = modelMapper.map(commentForm, Comment.class);
            commentRepository.save(comment);
            return getFormListByPost(commentForm.getPost());
        } else {   //todo 잘못된 요청 게시글이 존재하지 않습니다.
            return null;
        }
    }

    public List<CommentForm> getComments(long post_id) {

        Optional<Post> postById = postRepository.findById(post_id); //해당 게시글 존재여부
        if (postById.isPresent()) {

            return getFormListByPost(postById.get());
        } else {   //todo 잘못된 요청 게시글이 존재하지 않습니다.
            return null;
        }
    }

    public List<CommentForm> update(CommentForm commentForm) {
        Optional<Post> postById = postRepository.findById(commentForm.getPost().getPost_id()); //해당 게시글 존재여부
        if (postById.isPresent()) {
            Comment comment = modelMapper.map(commentForm, Comment.class);
            commentRepository.save(comment);
            return getFormListByPost(commentForm.getPost());
        } else {   //todo 잘못된 요청 게시글이 존재하지 않습니다.
            return null;
        }
    }

    public List<CommentForm> delete(CommentForm commentForm) {
        Comment comment = modelMapper.map(commentForm, Comment.class);
        commentRepository.delete(comment);
        return getFormListByPost(commentForm.getPost());
    }

    public List<CommentForm> getFormListByPost (Post post){
        List<CommentForm> commentForms = new ArrayList<>();
        List<Comment> allByPost = commentRepository.findAllByPost(post);
        for (Comment comment : allByPost) {
            commentForms.add(modelMapper.map(comment,CommentForm.class));
        }
        return commentForms;
    }
}

