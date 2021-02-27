package org.sid.service;

import java.util.List;

import org.sid.dto.CommentDto;
import org.sid.exceptions.PostNotFoundException;
import org.sid.exceptions.SpringRedditException;
import org.sid.exceptions.UsernameNotFoundException;
import org.sid.mapper.CommentMapper;
import org.sid.model.NotificationEmail;
import org.sid.model.Post;
import org.sid.model.User;
import org.sid.repository.CommentRepository;
import org.sid.repository.PostRepository;
import org.sid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.util.stream.Collectors.toList;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private CommentMapper commentsMapper;
    @Autowired
    private MailContentBuilder mailContentBuilder;
    @Autowired
    private MailService mailService;
    
    @org.springframework.transaction.annotation.Transactional
    public void save(CommentDto commentDto) {
	Post post = postRepository.findById(commentDto.getPostId()).orElseThrow(() -> new SpringRedditException("No post found for the given id: " + commentDto.getPostId()));
	// We must not use the following (line) solution because we must get the connected (signed in) user, because it's him who comments the post
	//User user = userRepository.findByUsername(commentsDto.getUserName()).orElseThrow(() -> new SpringRedditException("No user found for the given username: " + commentsDto.getUserName()));
	User user = authService.getCurrentUser();
	commentRepository.save(commentsMapper.map(commentDto, post, user));
	
	// Check if the commentator is not the post's owner
	if(post.getUser() != user) {
	    String message = mailContentBuilder.build(String.format("Hello %s, %s posted a comment on your post %s", post.getUser().getUsername(), user.getUsername(), post.getUrl()));
	    String subject = user.getUsername() + " commented your post";
	    sendCommentEmail(subject, message, post.getUser());
	}
    }

    private void sendCommentEmail(String subject, String message, User user) {
	mailService.sendEmail(new NotificationEmail(subject, user.getEmail(), message));
	
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsForPost(Long postId) {
	// First: we verify that the post exists
	Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("No post found for the given id: " + postId));
	// Then, if the post is not null
	return commentRepository.findByPost(post)
		.stream()
		.map(commentsMapper::mapToDto)
		.collect(toList());
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsForUser(String userName) {
	// First: we verify that the post exists
	User user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("No user found for the given username: " + userName));
	return commentRepository.findByUser(user)
		.stream()
		.map(commentsMapper::mapToDto)
		.collect(toList());
    }

}
