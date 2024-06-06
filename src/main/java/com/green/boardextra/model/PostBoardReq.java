package com.green.boardextra.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostBoardReq {
    private String title;
    private String contents;
    private String writer;
}
