package com.example.blog.service;

import com.example.blog.dto.request.PostRequest;
import com.example.blog.dto.response.ApiResponse;
import com.example.blog.model.Post;
import com.example.blog.repository.PostRepository;
import com.example.blog.security.service.UserAuth;
import com.example.blog.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    String notFound = "This article doesn't exist yet";

    public ResponseEntity<?> all(int page, int perPage){
        int currentPage = page <= 1 ? 0 : page - 1;
        Pageable pageable = PageRequest.of(currentPage, perPage, Sort.by("id").descending());
        return ApiResponse.success(postRepository.findAll(pageable));
    }

    public ResponseEntity<?> store(PostRequest postRequest) {
        UserAuth auth = (UserAuth) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = new Post();
        post.setAuthor_id(auth.getId());
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        String slug = Helper.toSlug(postRequest.getTitle());
        while (postRepository.existsBySlug(slug)) {
            slug = Helper.toSlug(slug + "-" + String.valueOf(new Date().getTime()).substring(0, 5));
        }
        post.setSlug(slug);
        postRepository.save(post);
        return ApiResponse.success("Article created successfully ", post);
    }

    public ResponseEntity<?> update(PostRequest postRequest, Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            String slug = Helper.toSlug(postRequest.getTitle());
            if (!postRequest.getTitle().equals(post.getTitle())) {
                while (postRepository.existsBySlug(slug)) {
                    slug = Helper.toSlug(slug + "-" + String.valueOf(new Date().getTime()).substring(0, 5));
                }
                post.setSlug(slug);
            }
            post.setTitle(postRequest.getTitle());
            post.setContent(postRequest.getContent());
            postRepository.save(post);
            return ApiResponse.success("Article updated successfully ", post);
        }
        return ApiResponse.notFound(notFound);
    }

    public ResponseEntity<?> delete(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            postRepository.delete(optionalPost.get());
            return ApiResponse.success("Article deleted successfully ");
        }
        return ApiResponse.notFound(notFound);
    }
}
