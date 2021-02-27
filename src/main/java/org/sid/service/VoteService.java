package org.sid.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.sid.dto.VoteDto;
import org.sid.exceptions.PostNotFoundException;
import org.sid.exceptions.SpringRedditException;
import org.sid.model.Post;
import org.sid.model.Vote;
import org.sid.model.Vote.VoteBuilder;
import org.sid.model.VoteTypeEnum;
import org.sid.repository.PostRepository;
import org.sid.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class VoteService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    VoteRepository voteRepository;
    @Autowired
    AuthService authService;
    
    public void vote(VoteDto voteDto) {
	Post post = postRepository.findById(voteDto.getPostId()).orElseThrow(() -> new PostNotFoundException("No post found for the given id: " + voteDto.getPostId()));
	// Because the post contains many votes, we have to search with post and user as criteria
	Optional<Vote> vote = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
	if(vote.isPresent() && vote.get().getVoteType().equals(voteDto.getVoteType())) {
	    throw new SpringRedditException("You have already " + vote.get().getVoteType() + "d this post");
	}
	// Otherwise
	if(voteDto.getVoteType().equals(VoteTypeEnum.UPVOTE)) {
	    post.setVoteCount(post.getVoteCount() + 1);
	}else {
	    post.setVoteCount(post.getVoteCount() - 1);
	}
	// Save modifications
	postRepository.save(post);
	// if the vote is already saved in the DB, we must modify its type value
	// because we are sure that it is changed, otherwise the exception would be thrown
	if(vote.isPresent()) {
	    vote.get().setVoteType(voteDto.getVoteType());
	    voteRepository.save(vote.get());
	}else {
	    voteRepository.save(mapToVote(voteDto, post));
	}
    }
    
    // mapper from VoteDto to Vote
    private Vote mapToVote(VoteDto voteDto, Post post) {

        VoteBuilder vote = Vote.builder();

        if(voteDto != null && post != null) {
            vote.voteType(voteDto.getVoteType());
            vote.post(post);
            vote.user(authService.getCurrentUser());
        }
        return vote.build();
    }
}
