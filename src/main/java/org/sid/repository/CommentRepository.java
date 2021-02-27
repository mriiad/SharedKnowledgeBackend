package org.sid.repository;

import java.util.List;

import org.sid.model.Comment;
import org.sid.model.Post;
import org.sid.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>{

    List<Comment> findByPost(Post post);

    List<Comment> findByUser(User user);

}
