package moais.todoManage.msjo.domain.todo.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import moais.todoManage.msjo.config.exception.BusinessException;
import moais.todoManage.msjo.domain.todo.dto.req.TodoCreateReq;
import moais.todoManage.msjo.domain.todo.repository.TodoRepository;
import moais.todoManage.msjo.entity.common.enums.TodoStatus;
import moais.todoManage.msjo.entity.domain.Member;
import moais.todoManage.msjo.entity.domain.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * packageName    : moais.todoManage.msjo.domain.todo.service
 * fileName       : TodoService
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TodoService {

    TodoRepository todoRepository;

    public Todo createTodo(Member member, TodoCreateReq request) {

        Todo todo = Todo.create(TodoStatus.TODO, request.getContents(), request.getComment(), request.getScheduledAt(), member);

        try {
            return todoRepository.save(todo);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return Todo.createEmptyObject();
        }

    }

    @Transactional(readOnly = true)
    public Todo findById(Member member, Long seq) {
        return todoRepository.findByMemberAndSeq(member, seq);
    }

    @Transactional(readOnly = true)
    public Page<Todo> findTodos(Member member, Pageable pageable) {
        return todoRepository.findByMemberOrderByCreatedAtDesc(member, pageable);
    }

    public Todo findLastestTodo(Member member) {
        Page<Todo> todos = findTodos(member, PageRequest.of(0, 1));
        return todos.isEmpty() ? null : todos.getContent().get(0);
    }

    public Todo changeStatus(Member member, Long seq, TodoStatus todoStatus) {

        Todo todo = findById(member, seq);

        if (Objects.isNull(todo)) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "TODO");
        }

        switch (todoStatus) {
            case TODO -> todo.toTODO();
            case DONE -> todo.toDone();
            case PENDING -> todo.toPending();
            case IN_PROGRESS -> todo.toProgress();
        }

        return todo;
    }
}
