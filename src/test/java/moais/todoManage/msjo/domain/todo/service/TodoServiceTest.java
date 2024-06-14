package moais.todoManage.msjo.domain.todo.service;

import moais.todoManage.msjo.config.exception.BusinessException;
import moais.todoManage.msjo.domain.member.dto.req.MemberCreateReq;
import moais.todoManage.msjo.domain.member.service.MemberService;
import moais.todoManage.msjo.domain.todo.dto.req.TodoCreateReq;
import moais.todoManage.msjo.entity.common.enums.TodoStatus;
import moais.todoManage.msjo.entity.domain.Member;
import moais.todoManage.msjo.entity.domain.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName    : moais.todoManage.msjo.domain.todo.service
 * fileName       : TodoServiceTest
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */

@SpringBootTest
@Transactional(readOnly = true)
@ActiveProfiles("test")
class TodoServiceTest {

    @Autowired MemberService memberService;
    @Autowired TodoService todoService;

    Member member;

    @BeforeEach
    public void setting() {
        MemberCreateReq request = new MemberCreateReq("msjo", "one", "qwe123!@#");
        Member member = memberService.createUser(request);
        this.member = member;
    }

    @Test
    public void 사용자는_TODO를_N개_생성할_수_있어야_한다() throws Exception {
    
        Todo todo1 = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );

        Todo todo2 = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );

        //then
        assertNotNull(todo1);
        assertNotNull(todo2);

    }
    
    @Test
    public void 사용자는_자신이_가장_최근에_작성한_TODO를_조회할_수_있어야_한다() throws Exception {
    
        todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );

        Thread.sleep(1000 * 1);

        Todo todo2 = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 40번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );

        Todo findlastestTodo = todoService.findLastestTodo(member);

        assertEquals(findlastestTodo.getContents(), todo2.getContents());

        //then
    
    }
    
    @Test
    public void 사용자는_자신이_작성한_TODO의_목록을_조회할_수_있어야_한다() throws Exception {
    
        IntStream.range(1, 8).forEach(i ->
            todoService.createTodo(member,
                new TodoCreateReq("패널티킥 " + i + "0번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
            )
        );

        //when
        Page<Todo> todos = todoService.findTodos(member, PageRequest.of(2, 3));

        //then
        assertEquals(todos.getTotalElements(), 7);
        assertEquals(todos.getContent().size(), 1);
    }

    @Test
    public void CAN_TODO_TO_DONE() throws Exception {

        //given
        Todo todo = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = todo.getSeq();

        todo.toDone();
        Todo doneTodo = todoService.findById(member, todo.getSeq());
        assertEquals(TodoStatus.DONE, doneTodo.getStatus());

    }

    @Test
    public void CAN_TODO_TO_PROGRESS() throws Exception {

        //given
        Todo todo = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = todo.getSeq();

        todo.toProgress();
        Todo progressTodo = todoService.findById(member, todo.getSeq());
        assertEquals(TodoStatus.IN_PROGRESS, progressTodo.getStatus());

    }

    @Test
    public void CANNOT_TODO_TO_PENDING() throws Exception {

        //given
        Todo todo = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = todo.getSeq();

        assertThrows(BusinessException.class, () -> todo.toPending());

    }

    @Test
    public void CAN_PROGRESS_TO_TODO() throws Exception {

        //given
        Todo todo = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = todo.getSeq();
        todo.toProgress();

        todo.toTODO();
        assertEquals(TodoStatus.TODO, todo.getStatus());

    }

    @Test
    public void CAN_PROGRESS_TO_DONE() throws Exception {

        //given
        Todo todo = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = todo.getSeq();
        todo.toProgress();

        todo.toDone();
        assertEquals(TodoStatus.DONE, todo.getStatus());

    }

    @Test
    public void CAN_PROGRESS_TO_PENDING() throws Exception {

        //given
        Todo todo = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = todo.getSeq();
        todo.toProgress();

        todo.toPending();
        assertEquals(TodoStatus.PENDING, todo.getStatus());

    }

    @Test
    public void CAN_DONE_TO_TODO() throws Exception {

        //given
        Todo todo = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = todo.getSeq();
        todo.toDone();

        todo.toTODO();
        assertEquals(TodoStatus.TODO, todo.getStatus());

    }

    @Test
    public void CAN_DONE_TO_PROGRESS() throws Exception {

        //given
        Todo todo = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = todo.getSeq();
        todo.toDone();

        todo.toProgress();
        assertEquals(TodoStatus.IN_PROGRESS, todo.getStatus());

    }

    @Test
    public void CANNOT_DONE_TO_PENDING() throws Exception {

        //given
        Todo todo = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = todo.getSeq();
        todo.toDone();

        assertThrows(BusinessException.class, () -> todo.toPending());

    }

    @Test
    public void CAN_PENDING_TO_TODO() throws Exception {

        //given
        Todo todo = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = todo.getSeq();
        todo.toProgress();
        todo.toPending();

        todo.toTODO();
        assertEquals(TodoStatus.TODO, todo.getStatus());

    }
    @Test
    public void CAN_PENDING_TO_PROGRESS() throws Exception {

        //given
        Todo todo = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = todo.getSeq();
        todo.toProgress();
        todo.toPending();

        todo.toProgress();
        assertEquals(TodoStatus.IN_PROGRESS, todo.getStatus());

    }
    @Test
    public void CAN_PENDING_TO_DONE() throws Exception {

        //given
        Todo todo = todoService.createTodo(member,
                new TodoCreateReq("패널티킥 30번 차기", "실내 축구장 이용", LocalDateTime.now().plusDays(1))
        );
        Long seq = todo.getSeq();
        todo.toProgress();
        todo.toPending();

        todo.toDone();
        assertEquals(TodoStatus.DONE, todo.getStatus());

    }

}