package org.sid.dto;

import org.sid.model.VoteTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    private VoteTypeEnum voteType;
    private Long postId;
}
