package com.ownwear.app.service;

import com.ownwear.app.dto.CommentForm;
import com.ownwear.app.dto.PostForm;
import com.ownwear.app.entity.Comment;
import com.ownwear.app.entity.Post;
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
        Optional<Post> postById = postRepository.findById(commentForm.getPost().getPostid()); //해당 게시글 존재여부
        if (postById.isPresent()) {
            Comment comment = modelMapper.map(commentForm, Comment.class);
            commentRepository.save(comment);
            return getFormListByPost(commentForm.getPost());
        } else {   //todo 잘못된 요청 게시글이 존재하지 않습니다.
            return null;
        }
    }

    public List<CommentForm> getComments(long postid) {

        Optional<Post> postById = postRepository.findById(postid); //해당 게시글 존재여부
        if (postById.isPresent()) {
            PostForm map = modelMapper.map(postById.get(), PostForm.class);
            return getFormListByPost(map);
        } else {   //todo 잘못된 요청 게시글이 존재하지 않습니다.
            return null;
        }
    }

    public List<CommentForm> update(CommentForm commentForm) {
        Optional<Post> postById = postRepository.findById(commentForm.getPost().getPostid()); //해당 게시글 존재여부
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

    public List<CommentForm> getFormListByPost (PostForm postForm){
        List<CommentForm> commentForms = new ArrayList<>();
        Post post = modelMapper.map(postForm, Post.class);
        List<Comment> allByPost = commentRepository.findAllByPost(post);
        for (Comment comment : allByPost) {
            commentForms.add(modelMapper.map(comment,CommentForm.class));
        }
        return commentForms;
    }
}

