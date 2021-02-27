package org.sid.repository;

import java.util.List;

import org.sid.model.Post;
import org.sid.model.Subreddit;
import org.sid.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

    public List<Post> findBySubreddit(Subreddit subreddit);

    public List<Post> findByUser(User user);

}
