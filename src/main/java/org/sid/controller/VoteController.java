package org.sid.controller;

import org.sid.dto.VoteDto;
import org.sid.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/votes")
@AllArgsConstructor
public class VoteController {

    @Autowired
    VoteService voteService;
    
    @PostMapping
    public ResponseEntity<Void> getAllCommentsForPost(@RequestBody VoteDto voteDto) {
	voteService.vote(voteDto);
	return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
