package com.springboot.blog.payload;

import lombok.Data;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Data
//used @data annotation to avoid getter, setter ,to string codes
public class PostDto {
    private long id;

    //Post title,description,content should not be null or empty , they should have min charas
    @NotEmpty
    @Size(min=2,message="Post title should have minimum of 2 characters")
    private String title;
    @NotEmpty
    @Size(min=10, message="Post description should have minimum of 10 characters")
    private String description;
    @NotEmpty
    private String content;
    private Set<CommentDto> comments;
}
