package com.springboot.blog.entity;

import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import java.util.*;
import jakarta.persistence.*;
import java.util.HashSet;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name="posts", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})}
)
public class  Post {
    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(name="title",nullable = false)
    private String title;
    @Column(name="description", nullable = false)
    private String description;
    @Column(name="content", nullable = false)
    private String content;
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Comment> comments=new HashSet<>();
}
