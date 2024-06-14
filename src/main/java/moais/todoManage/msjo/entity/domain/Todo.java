package moais.todoManage.msjo.entity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import moais.todoManage.msjo.config.exception.BusinessException;
import moais.todoManage.msjo.entity.common.audit.BaseTime;
import moais.todoManage.msjo.entity.common.enums.TodoStatus;
import moais.todoManage.msjo.util.JSONUtil;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * packageName    : moais.todoManage.msjo.entity.domain
 * fileName       : Todo
 * author         : ms.jo
 * date           : 2024. 6. 13.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 13.        ms.jo       최초 생성
 */
@Entity
@Getter
@DynamicUpdate
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "member")
@Comment("할 일")
@Slf4j
public class Todo extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("할 일 순차번호")
    Long seq;

    @Enumerated(EnumType.STRING)
    @Comment("할 일 상태")
    TodoStatus status;

    @Comment("내용")
    String contents;

    @Comment("코멘트")
    String comment;

    @Comment("수행 예정 일시")
    LocalDateTime scheduledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_seq")
    @Comment("사용자")
    //prevent infinite recursive
    @JsonIgnore
    Member member;

    @OneToMany(mappedBy = "todo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<TodoHistory> histories = Lists.newArrayList();

    protected Todo(TodoStatus status, String contents, String comment, LocalDateTime scheduledAt, Member member) {
        this.status = status;
        this.contents = contents;
        this.comment = comment;
        this.scheduledAt = scheduledAt;
        this.member = member;
    }

    public static Todo create(TodoStatus status, String contents, String comment, LocalDateTime scheduledAt, Member member) {

        Todo todo = new Todo(status, contents, comment, scheduledAt, member);

        todo.getHistories().add(TodoHistory.create(null, JSONUtil.toJSONString(todo), todo));

        member.getTodos().add(todo);

        return todo;

    }

    public static Todo createEmptyObject() {
        return new Todo();
    }

    /**
     * TODO history 관리를 어떻게 할 지 고민 중. 남긴다면 history 생성은 이 곳에서만. -- DONE
     *
     * @return
     */

    public boolean toTODO() {
        return updateStatus(TodoStatus.TODO);
    }

    public boolean toProgress() {
        return updateStatus(TodoStatus.IN_PROGRESS);
    }

    public boolean toDone() {
        return updateStatus(TodoStatus.DONE);
    }

    public boolean toPending() {
        if (Objects.equals(this.status, TodoStatus.IN_PROGRESS)) {
            return updateStatus(TodoStatus.PENDING);
        }

        log.error("Changing to the Pending state is only possible when in the Progress state.");

        throw new BusinessException(HttpStatus.BAD_REQUEST);
    }

    private boolean updateStatus(TodoStatus newStatus) {
        String before = JSONUtil.toJSONString(this);
        this.status = newStatus;
        String after = JSONUtil.toJSONString(this);

        TodoHistory history = new TodoHistory(before, after, this);
        this.histories.add(history);

        return true;
    }


}
