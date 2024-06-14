package moais.todoManage.msjo.domain.todo.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import moais.todoManage.msjo.domain.todo.dto.res.TodoFindRes;
import moais.todoManage.msjo.entity.domain.Todo;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : moais.todoManage.msjo.domain.todo.mapper
 * fileName       : TodoMapper
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */
@Configuration
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoMapper {

    public TodoFindRes toTodoFindRes(Todo todo) {
        return new TodoFindRes(todo.getSeq(), todo.getContents(), todo.getComment(), todo.getStatus(), todo.getScheduledAt());
    }

}
