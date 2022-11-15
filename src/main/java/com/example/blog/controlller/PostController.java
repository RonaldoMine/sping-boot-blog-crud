package com.example.blog.controlller;

import com.example.blog.dto.request.PostRequest;
import com.example.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/posts")
public class PostController {
    @Autowired
    PostService postService;

    @GetMapping("")
    public ResponseEntity<?> all(@RequestParam int page, @RequestParam int perPage) {
        return postService.all(page, perPage);
    }

    @PostMapping("store")
    public ResponseEntity<?> store(@RequestBody PostRequest postRequest) {
        return postService.store(postRequest);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@RequestBody PostRequest postRequest, @PathVariable Long id) {
        return postService.update(postRequest, id);
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return postService.delete(id);
    }

}
