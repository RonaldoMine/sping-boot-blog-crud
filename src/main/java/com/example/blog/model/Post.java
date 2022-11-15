package com.example.blog.model;

import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long author_id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String slug;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String content;

    private Timestamp created_at = new Timestamp(new Date().getTime());

    @UpdateTimestamp
    private Timestamp updated_at = new Timestamp(new Date().getTime());

}
