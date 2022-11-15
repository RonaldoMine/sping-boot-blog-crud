package com.example.blog.controlller;

import com.example.blog.dto.request.CommentRequest;
import com.example.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/comments")
public class CommentController {
    @Autowired
    CommentService commentService;

    @GetMapping("{postId}")
    public ResponseEntity<?> all(@RequestParam int page, @RequestParam int perPage, @PathVariable Long postId) {
        return commentService.all(postId, page, perPage);
    }

    @PostMapping("store/{postId}")
    public ResponseEntity<?> store(@RequestBody CommentRequest commentRequest, @PathVariable Long postId) {
        return commentService.store(postId, commentRequest);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@RequestBody CommentRequest commentRequest, @PathVariable Long id) {
        return commentService.update(commentRequest, id);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return commentService.delete(id);
    }

}
