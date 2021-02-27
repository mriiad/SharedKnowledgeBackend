package org.sid.controller;

import java.util.List;


import org.sid.dto.PostRequest;
import org.sid.dto.PostResponse;
import org.sid.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
	postService.save(postRequest);
	return new ResponseEntity<Void>(HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id) {
	return postService.getPost(id);
    }
    
    @CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
    @GetMapping("/")
    public ResponseEntity<List<PostResponse>> getAllPosts(){
	return ResponseEntity.status(HttpStatus.OK)
		.body(postService.getAllPosts());
    }
    
    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(@PathVariable Long id){
	return ResponseEntity.status(HttpStatus.OK)
		.body(postService.getPostsBySubreddit(id));
    }
    
    // The @PathVariable must be the same as the parameter in the URI
    // in this case {username} === username
    // otherwise: "Missing URI template variable 'username' for method parameter of type String"
    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username){
	return ResponseEntity.status(HttpStatus.OK)
		.body(postService.getPostsByUsername(username));
    }
}
