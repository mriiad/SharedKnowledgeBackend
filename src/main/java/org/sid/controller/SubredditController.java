package org.sid.controller;

import java.util.List;

import org.sid.dto.SubredditDto;
import org.sid.service.SubredditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subreddit")
public class SubredditController {

    @Autowired
    private SubredditService subredditService;
    
    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
	return ResponseEntity.status(HttpStatus.CREATED)
	.body(subredditService.save(subredditDto));
    }
    
    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddit() {
	return ResponseEntity
		.status(HttpStatus.OK)
		.body(subredditService.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id) {
	return ResponseEntity
		.status(HttpStatus.OK)
		.body(subredditService.getSubreddit(id));
    }
}
