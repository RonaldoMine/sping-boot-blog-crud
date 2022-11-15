package com.example.blog.service;

import com.example.blog.dto.request.CommentRequest;
import com.example.blog.dto.response.ApiResponse;
import com.example.blog.model.Comment;
import com.example.blog.model.Post;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.security.service.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    String notFound = "This comment doesn't exist yet";

    public ResponseEntity<?> all(Long postId, int page, int perPage) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            int currentPage = page <= 1 ? 0 : page - 1;
            Pageable pageable = PageRequest.of(currentPage, perPage, Sort.by("id").descending());
            return ApiResponse.success(commentRepository.findAllByPost(post, pageable));
        }
        return ApiResponse.notFound("Post not find");
    }

    public ResponseEntity<?> store(Long postId, CommentRequest commentRequest) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            UserAuth auth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Comment comment = new Comment();
            comment.setAuthor_id(auth.getId());
            comment.setContent(commentRequest.getContent());
            comment.setPost(optionalPost.get());
            commentRepository.save(comment);
            return ApiResponse.success("Comment posted successfully ", comment);
        }
        return ApiResponse.notFound("Post not find");
    }

    public ResponseEntity<?> update(CommentRequest commentRequest, Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setContent(commentRequest.getContent());
            commentRepository.save(comment);
            return ApiResponse.success("Comment updated successfully", comment);
        }
        return ApiResponse.notFound(notFound);
    }

    public ResponseEntity<?> delete(Long id) {
        Optional<Comment> checkPost = commentRepository.findById(id);
        if (checkPost.isPresent()) {
            commentRepository.delete(checkPost.get());
            return ApiResponse.success("Comment deleted successfully ");
        }
        return ApiResponse.notFound(notFound);
    }
}
