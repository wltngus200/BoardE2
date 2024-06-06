package com.green.boardextra;

import com.green.boardextra.model.GetBoardReq;
import com.green.boardextra.model.PostBoardReq;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/board")
public class BoardController {
    private final BoardService service;

    @PostMapping
    public int postBoard(@RequestBody PostBoardReq p) {
        return service.postBoard(p);
    }

    @PostMapping("file")
    public int postBoardFile(@RequestPart MultipartFile pic
                           , @RequestPart PostBoardReq p) {
        log.info("pic: {}", pic.getOriginalFilename());
        log.info("p: {}", p);
        return 10;
    }

    //Query String - RequestParam (page, size)
    @GetMapping
    public int getBoard(@RequestParam int page, @RequestParam int size, @RequestParam String search) {
        log.info("page: {}", page);
        log.info("size: {}", size);
        service.getBoard(page, size, search);
        return 1;
        //return "{ \"test\": \"반가워\", \"age\": 23 }";
    }
    @GetMapping("/object")
    public int getBoardObject(@RequestParam int page, @RequestParam int size, @RequestParam String search) {
        log.info("page: {}", page);
        log.info("size: {}", size);
        GetBoardReq p=new GetBoardReq();
        p.setPage(page);
        p.setSize(size);
        p.setSearch(search);
        service.getBoard(p);
        return 1;
    }

    @GetMapping("/object2")
    public int getBoardObject2(@ParameterObject/*swagger*/ @ModelAttribute GetBoardReq p){
        //search는 항상 하는 것이 아니라 required를 풀어야 함(파람: false하면 0과 null로 기본값으로 인해 문제 발생)
        // validation으로 강제화가능 일부러 에러를 throw
        service.getBoard(p);//내가 아니라 스프링이 해준다 //서블릿이 해 줌
        return 1;
    }

    @DeleteMapping
    public int deleteBoard(@RequestParam(required = false) Integer page, @RequestParam int size) {

        //int num = null;
        log.info("page: {}", page);
        log.info("size: {}", size);
        return 1;
        //return "{ \"test\": \"반가워\", \"age\": 23 }";
    }

    @GetMapping("req")
    public int getBoardReq(HttpServletRequest req) {
        BoardGetReq model = new BoardGetReq();

        String strPage = req.getParameter("page");
        int page = 0;
        if(strPage != null) {
            page = Integer.parseInt(strPage);
        }
        log.info("page: {}", page);
        log.info("strPage: {}", strPage);
        return 12;
    }


    @GetMapping("model")
    public int getBoardModel(@ModelAttribute BoardGetReq p) {
        log.info("p: {}", p);
        return 15;
    }

    @GetMapping("model2")
    public int getBoardModel2(@ParameterObject BoardGetReq p) {
        log.info("p2: {}", p);
        return 15;
    }
}

@Getter
@Setter
@ToString
class BoardGetReq {
    private int page;
    private int size;
}










