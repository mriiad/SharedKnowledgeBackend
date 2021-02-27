package org.sid.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.sid.dto.CommentDto;
import org.sid.model.Comment;
import org.sid.model.Post;
import org.sid.model.User;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    
    CommentMapper MAPPER = Mappers.getMapper(CommentMapper.class);
    
    @Mapping(target = "id", ignore = true) // because it's generated automatically, so we don't pass it when calling api
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment map(CommentDto commentsDto, Post post, User user);
    
    @Mapping(target = "postId", expression = "java(mapPostId(comment.getPost()))")
    @Mapping(target = "userName", expression = "java(mapUserName(comment.getUser()))")
    CommentDto mapToDto(Comment comment);
    
    default Long mapPostId(Post post) {return post.getPostId();}
    default String mapUserName(User user) {return user.getUsername();}
}
