package org.sid.model;

public enum VoteTypeEnum {

    UPVOTE(1),
    DOWNVOTE(-1),
    ;

    VoteTypeEnum(int direction) {
    }
}
