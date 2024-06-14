package moais.todoManage.msjo.domain.todo.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import moais.todoManage.msjo.entity.common.enums.TodoStatus;

import java.time.LocalDateTime;

/**
 * packageName    : moais.todoManage.msjo.domain.todo.dto.res
 * fileName       : TodoFindRes
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(title = "TODO 생성 응답 DTO")
@AllArgsConstructor
public class TodoFindRes {

    @Schema(example = "1")
    Long seq;

    @Schema(example = "프리킥 30개 차기")
    String contents;

    @Schema(example = "비오면 실내에서 진행")
    String comment;

    @Schema(example = "TODO", allowableValues = {"TODO", "IN_PROGRESS","DONE", "PENDING"})
    TodoStatus status;

    @Schema(example = "2024-06-23 14:00:00")
    LocalDateTime scheduledDate;

}
