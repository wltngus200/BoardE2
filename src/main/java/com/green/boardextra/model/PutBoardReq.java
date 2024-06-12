package com.green.boardextra.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PutBoardReq {
    private long boardId;
    private String title;
    private String contents;
    //writer는 변경 되어서는 안 됨

}
