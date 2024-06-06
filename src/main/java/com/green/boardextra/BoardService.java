package com.green.boardextra;

import com.green.boardextra.model.GetBoardReq;
import com.green.boardextra.model.GetBoardRes;
import com.green.boardextra.model.PostBoardReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper mapper;

    public int postBoard(PostBoardReq p) {
        return mapper.insBoard(p);
    }

    public void getBoard(int page, int size, String search){
        log.info("service");
    }
    public void getBoard2(GetBoardReq p){

    }
    public List<GetBoardRes> getBoard(GetBoardReq p){
        return mapper.selBoardList(p);
    }
}
