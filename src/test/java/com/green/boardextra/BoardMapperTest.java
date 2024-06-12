package com.green.boardextra; //패키지는 같지만 똑같은 공간에 있는 것은 아님(달라도 상관은 없음)

import com.green.boardextra.model.GetBoardReq;
import com.green.boardextra.model.GetBoardRes;
import com.green.boardextra.model.PostBoardReq;
import com.green.boardextra.model.PutBoardReq;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.*;
@MybatisTest //My batis를 테스트 //빈등록을 하면 스프링컨테이너도 같이 가동+객체화
@TestInstance(TestInstance.Lifecycle.PER_CLASS) //beforeAll에 static이 빠짐
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoardMapperTest {
    private final int INS_RECORD_SIZE=10;


    @Autowired private BoardMapper mapper;//스프링 컨테이너도 같이 가동 //final+Required로 안 됨
    //마이바티스가 자동 객체화(마이바티스가 xml을 전부 읽어 매칭되는 애 찾아 그것만 객체화)
    //mapper+xml=class파일 (DAO)만듦 + 빈등록
     //마이바티스가 얘만 클래스 파일을 만듦 (인터페이스의 빈 메소드의 빈 부분을 채움)
    //딱 1개의 주소값 주입 DI //세터도 생성자도 아닌데 넣어서 객체 생성이 됨
    //주소값 받은 이유: 얘를 테스트 하기 위해
    //얘는 누군가를 이용하지 않기 때문에 가짜는 필요 없다

    //테스트마다 DB의 데이터가 바뀌면 양질의 테스트가 불가능함


    //테스트 하나하나는 다 독립적 //실행 될 때 순서가 보장되지 X
    //Junit이 실행
    @BeforeAll //static은 위의 설정으로 뺄 수 있음 //테스트의 실행에서 1번 실행(테스트 개수와 상관X)
    static void beforeAll(){

    }

    @BeforeEach //테스트 어노테이션이 실행 전 1번 시행
    void beforeEach(){
        //각 작업마다 데이터를 지우고 다시
        mapper.delBoardAllForTest();


        PostBoardReq p=new PostBoardReq();
        for(int i=1;i<=INS_RECORD_SIZE; i++){
            p.setTitle(String.format("테스트 %d", i));
            p.setContents(String.format("테스트 내용 %d", i));
            p.setWriter(String.format("테스트 작성자 %d", i));
            mapper.insBoard(p);
        }
        GetBoardReq req=new GetBoardReq();
        req.setPage(1);
        req.setSize(20);
        //정말 10개의 인서트가 되었나 확인
        List<GetBoardRes> list=mapper.selBoardList(req);
        System.out.printf("list: %d\n", list.size());
        //트런케이트는 롤백이 안 되어 빠름
        for(GetBoardRes item:list){
            System.out.println(item); //AUTO INCREMENT는 롤백 x
        }
    }

    @Test
    void insBoard() {
        PostBoardReq p=new PostBoardReq();
        p.setTitle("그린컴퓨터"); //에러가 넘어왔을 때 오류가 나는지도 확인
        p.setContents("자바 백엔드");
        p.setWriter("홍길동");

        int affectedRows=mapper.insBoard(p); //메소드의 실행
        assertEquals(1, affectedRows, "영향받은 행이 1이 아니다."); //1이 넘어옴

        //메소드의 실행 전 후 전체 리스트를 들고 와서 비교했을 때 +1이 되었는가
        GetBoardReq req= new GetBoardReq();
        req.setPage(1);
        req.setSize(20);
        List<GetBoardRes> allList=mapper.selBoardList(req);
        assertEquals(INS_RECORD_SIZE+1, allList.size(), "이전과 변화가 없다");

        //실제 그 값이 들어갔는가(내림차순 정렬이기 때문에 [0]조회하면 방금 입력한 값
        GetBoardRes newRecord=allList.get(0);
        assertEquals(p.getTitle(), newRecord.getTitle(), "제목이 다름");
        assertEquals(p.getWriter(), newRecord.getWriter(), "작성자가 다름");
        assertNotNull(newRecord.getCreatedAt(), "날짜가 입력되지 않음");
    }

    @Test
    void selBoardList() {
        GetBoardReq req= new GetBoardReq();
        req.setPage(1);
        req.setSize(20);
        List<GetBoardRes> allList=mapper.selBoardList(req);
        assertEquals(INS_RECORD_SIZE, allList.size(), "레코드의 수가 다름");

        int checkVal=10;
        for(int i=0; i<INS_RECORD_SIZE; i++){
            GetBoardRes res= allList.get(i);

            assertThat(res.getBoardId(), greaterThan(0L));
            assertEquals(String.format("테스트 %d", checkVal), res.getTitle());
            assertEquals(String.format("테스트 작성자 %d", checkVal), res.getWriter());
            assertNotNull(res.getCreatedAt());
            checkVal--;
        }
    }
    @Test
    void updBoard(){
        GetBoardReq req= new GetBoardReq();
        req.setPage(1);
        req.setSize(20);
        List<GetBoardRes> beforeList=mapper.selBoardList(req);
        GetBoardRes originData=beforeList.get(0);

        PutBoardReq p=new PutBoardReq();
        p.setBoardId(originData.getBoardId());
        p.setTitle("완전히 다른 제목");
        p.setContents("완전히 다른 내용");

        int affectedRows=mapper.updBoard(p);
        assertEquals(1, affectedRows, "수정된 레코드 수가 다름");

        //실제 그 데이터로 바뀌었는지 확인
        //다시 한 번 select가 필요
        List<GetBoardRes> afterList=mapper.selBoardList(req);
        assertEquals(beforeList.size(), afterList.size(), "레코드 수가 변화가 되었다");

        GetBoardRes afterData=afterList.get(0);
        assertEquals(p.getTitle(), afterData.getTitle());
    }
}