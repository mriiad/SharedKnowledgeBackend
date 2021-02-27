package org.sid.service;

import javax.transaction.Transactional;

import org.sid.dto.SubredditDto;
import org.sid.exceptions.SpringRedditException;
import org.sid.mapper.SubredditMapper;
import org.sid.model.Subreddit;
import org.sid.repository.SubredditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static java.util.stream.Collectors.toList;

import java.util.List;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SubredditService {
    
    @Autowired
    private SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;
    
    @Transactional
    public SubredditDto save(SubredditDto subredditDto) {
	Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
	subredditDto.setId(save.getId());
	return subredditDto;
    }
    
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
	return subredditRepository.findAll()
		.stream()
		.map(subredditMapper::mapSubredditToDto) // this : the current element
		.collect(toList());
    }

    public SubredditDto getSubreddit(Long id) {
	Subreddit subreddit = subredditRepository.findById(id).orElseThrow(() -> new SpringRedditException("No subreddit found for the given id"));
	return subredditMapper.mapSubredditToDto(subreddit);
    } 
}
