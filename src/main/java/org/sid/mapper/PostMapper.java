package org.sid.mapper;

import java.time.Instant;
import java.util.Locale;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sid.dto.PostRequest;
import org.sid.dto.PostResponse;
import org.sid.model.Post;
import org.sid.model.Subreddit;
import org.sid.model.User;
import org.sid.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.marlonlom.utilities.timeago.TimeAgo;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    // Mapping to Post
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    // For the following two fields, they will be done by map struct expelicitly (it's clear for it)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "subreddit", source = "subreddit")
    @Mapping(target = "description", source = "postRequest.description") // To specify which one -> because subreddit also has a field named description
    @Mapping(target = "voteCount", expression = "java(new Integer(0))")
    public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);
    
    @Mapping(target = "id", source = "postId")
    @Mapping(target = "userName", source = "user.username")
    @Mapping(target = "subredditName", source = "subreddit.name")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(duration(post.getCreatedDate()))")
    public abstract PostResponse mapToDto(Post post);
    
    protected Integer commentCount(Post post) {return commentRepository.findByPost(post).size();}
    protected String duration(Instant createdDate) {
	// TO DISPLAY THE MESSAGE IN FRENCH
//	Locale LocaleBylanguageTag = Locale.forLanguageTag("fr"); 
//	TimeAgoMessages messages = new TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build();
//	return TimeAgo.using(createdDate.toEpochMilli(), messages);
	return TimeAgo.using(createdDate.toEpochMilli());}
}
