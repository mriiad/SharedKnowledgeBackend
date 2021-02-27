package org.sid.service;

import java.util.List;

import javax.transaction.Transactional;
import org.sid.dto.PostRequest;
import org.sid.dto.PostResponse;
import org.sid.exceptions.SpringRedditException;
import org.sid.mapper.PostMapper;
import org.sid.model.Post;
import org.sid.model.Subreddit;
import org.sid.model.User;
import org.sid.repository.PostRepository;
import org.sid.repository.SubredditRepository;
import org.sid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.util.stream.Collectors.toList;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private SubredditRepository subredditRepository;
    @Autowired
    private UserRepository userRepository;
    
    @org.springframework.transaction.annotation.Transactional
    public void save(PostRequest postRequest) {
	Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
		.orElseThrow(() -> new SpringRedditException("No subreddit found for the given name " + postRequest.getSubredditName()));
	User user = authService.getCurrentUser();
	postRepository.save(postMapper.map(postRequest, subreddit, (User) user));
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
	Post post = postRepository.findById(id).orElseThrow(() -> new SpringRedditException("No post found for the given id " + id));
	return postMapper.mapToDto(post);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
	return postRepository.findAll()
		.stream()
		.map(postMapper::mapToDto)
		.collect(toList());
    }

    public List<PostResponse> getPostsBySubreddit(Long id) {
	return postRepository.findBySubreddit(subredditRepository.findById(id).orElseThrow(() -> new SpringRedditException("No subreddit found for the given id " + id)))
		.stream()
		.map(postMapper::mapToDto)
		.collect(toList());
    }

    public List<PostResponse> getPostsByUsername(String username) {
	return postRepository.findByUser(userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("No user found for the given username " + username)))
		.stream()
		.map(postMapper::mapToDto)
		.collect(toList());
    }
}
