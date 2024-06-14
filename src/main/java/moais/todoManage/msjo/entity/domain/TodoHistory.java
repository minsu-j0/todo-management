package moais.todoManage.msjo.entity.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import moais.todoManage.msjo.entity.common.audit.BaseTime;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

/**
 * packageName    : moais.todoManage.msjo.entity.domain
 * fileName       : TodoHistory
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
@ToString(exclude = "todo")
@Comment("할 일 변경 내역")
public class TodoHistory extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("할 일 변경 내역 순차번호")
    Long seq;

    @Comment("변경 전 data")
    @Column(length = 2048)
    String beforeData;
    @Comment("변경 후 data")
    @Column(nullable = false, length = 2048)
    String afterData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_seq")
    @Comment("할 일 순차번호")
    //prevent infinite recursive
    @JsonIgnore
    Todo todo;

    protected TodoHistory(String before, String after, Todo todo) {
        this.beforeData = before;
        this.afterData = after;
        this.todo = todo;
    }

    public static TodoHistory create(String before, String after, Todo todo) {
        return new TodoHistory(before, after, todo);
    }

}
