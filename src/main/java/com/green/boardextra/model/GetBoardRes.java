package com.green.boardextra.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetBoardRes {
    private long boardId;
    private String title;
    private String writer;
    private String createdAt;
}
