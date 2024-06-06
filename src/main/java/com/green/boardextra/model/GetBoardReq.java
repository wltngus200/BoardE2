package com.green.boardextra.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetBoardReq {
    private int page;
    private int size;
    private String search;
    private int startIdx;

    public void setSize(int size) {
        this.size = size;
        this.startIdx=(page-1)*size; //이름 순서로 매핑해 줌 size>> aize가 되면 500에러
    }
}
