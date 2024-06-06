package com.green.boardextra.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetBoardRes {
    private long boardId;
    private String title;
    private String writer;
    private String createdAt;
}
