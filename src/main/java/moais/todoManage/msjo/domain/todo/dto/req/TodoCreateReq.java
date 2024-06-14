package moais.todoManage.msjo.domain.todo.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import moais.todoManage.msjo.entity.common.audit.BaseTime;
import moais.todoManage.msjo.entity.domain.Member;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * packageName    : moais.todoManage.msjo.domain.todo.dto.req
 * fileName       : TodoCreateReq
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
@Schema(title = "TODO 생성 요청 DTO")
@AllArgsConstructor
public class TodoCreateReq {

    @Schema(example = "프리킥 연습 30개")
    String contents;

    @Schema(example = "비오면 실내, 비 안오면 JC공원 에서")
    String comment;

    @Schema(example = "수행 예정 일시")
    LocalDateTime scheduledAt;

}
