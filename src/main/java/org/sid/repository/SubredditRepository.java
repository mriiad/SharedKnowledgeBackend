package org.sid.repository;

import java.util.Optional;

import org.sid.model.Subreddit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SubredditRepository  extends JpaRepository<Subreddit, Long>{

    public Optional<Subreddit> findByName(String name);

}