package moais.todoManage.msjo.domain.todo.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import moais.todoManage.msjo.entity.common.enums.TodoStatus;

/**
 * packageName    : moais.todoManage.msjo.domain.todo.dto.req
 * fileName       : TodoChangeStatusReq
 * author         : ms.jo
 * date           : 2024. 6. 15.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 15.        ms.jo       최초 생성
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "TODO 상태 변경 요청 DTO")
@AllArgsConstructor
public class TodoChangeStatusReq {

    @Schema(example = "TODO")
    TodoStatus status;

}
