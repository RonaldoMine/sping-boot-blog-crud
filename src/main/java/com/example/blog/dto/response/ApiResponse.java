package com.example.blog.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class ApiResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public ApiResponse(String message) {
        this.message = message;
    }

    public ApiResponse(Object data) {
        this.data = data;
    }

    public ApiResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public static ResponseEntity<?> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(message));
    }

    public static ResponseEntity<?> success(String message) {
        return ResponseEntity.ok(new ApiResponse(message));
    }

    public static ResponseEntity<?> success(String message, Object data) {
        return ResponseEntity.ok(new ApiResponse(message, data));
    }

    public static ResponseEntity<?> success(Object data) {
        return ResponseEntity.ok(new ApiResponse(data));
    }

}
