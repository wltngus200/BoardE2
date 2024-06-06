package com.green.boardextra;

import com.green.boardextra.model.GetBoardReq;
import com.green.boardextra.model.GetBoardRes;
import com.green.boardextra.model.PostBoardReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    int insBoard(PostBoardReq p);
    List<GetBoardRes> selBoardList(GetBoardReq p);
}
