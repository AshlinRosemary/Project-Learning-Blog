package com.springboot.blog.service.impl;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
//below is a Springbean class
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private ModelMapper mapper;
    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
        this.mapper=mapper;
        this.postRepository = postRepository;
    }
    @Override
    public PostDto CreatePost(PostDto postDto) {
        //convert DTO to entity
        Post post = mapToEntity(postDto);
        Post newPost=postRepository.save(post);
        //convert entity into DTO
        PostDto postResponse = mapToDTO(newPost);
        return postResponse;
    }
    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy,String sortDir) {
        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        //Creating Pageable Instance
        Pageable pageable=PageRequest.of(pageNo,pageSize, sort);
        Page<Post> posts=postRepository.findAll(pageable);
        //get content for page object
        List<Post> listOfPosts= posts.getContent();

        List <PostDto> content=listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }
    @Override
    public PostDto getPostById(long id) {
        Post post=postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        //get post by ID frm the database otherwise throw the exception.
        Post post=postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost=postRepository.save(post);
        return mapToDTO(updatedPost);

    }

    @Override
    public void deletePostbyId(long id) {
        //get post by ID from Database
        Post post=postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);
    }

    //Here we are creating a method(mapToDTO for Coversion of Entity to Dto
    //So we can call this in all http methods without writing codes to one of the method
    private PostDto mapToDTO(Post post) {
        PostDto postDto=mapper.map(post,PostDto.class);
    /*PostDto postDto=new PostDto();
    postDto.setId(post.getId());
    postDto.setTitle(post.getTitle());
    postDto.setContent(post.getContent());
    postDto.setDescription(post.getDescription());
    */
        return postDto;
    }
    private Post mapToEntity(PostDto postDto){
        Post post=mapper.map(postDto,Post.class);
        /*Post post=new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());*/
        return post;
    }

}
