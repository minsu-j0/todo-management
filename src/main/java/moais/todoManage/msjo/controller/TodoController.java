package moais.todoManage.msjo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import moais.todoManage.msjo.config.security.SecurityUserDetails;
import moais.todoManage.msjo.domain.auth.dto.res.JwtIssueRes;
import moais.todoManage.msjo.domain.auth.service.TokenService;
import moais.todoManage.msjo.domain.member.dto.req.MemberCreateReq;
import moais.todoManage.msjo.domain.member.dto.req.MemberInactiveReq;
import moais.todoManage.msjo.domain.member.dto.req.MemberModifyNicknameReq;
import moais.todoManage.msjo.domain.member.service.MemberService;
import moais.todoManage.msjo.domain.todo.dto.req.TodoChangeStatusReq;
import moais.todoManage.msjo.domain.todo.dto.req.TodoCreateReq;
import moais.todoManage.msjo.domain.todo.dto.res.TodoFindRes;
import moais.todoManage.msjo.domain.todo.mapper.TodoMapper;
import moais.todoManage.msjo.domain.todo.service.TodoService;
import moais.todoManage.msjo.entity.common.enums.TodoStatus;
import moais.todoManage.msjo.entity.domain.Member;
import moais.todoManage.msjo.entity.domain.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


/**
 * packageName    : moais.todoManage.msjo.controller
 * fileName       : TodoController
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */
@SecurityRequirement(name = "Bearer Authentication")
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer"
)
@RestController
@RequestMapping("/todos")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@Tag(name = "TODO API")
public class TodoController {

    MemberService memberService;
    TodoService todoService;
    TodoMapper todoMapper;

    /**
     * CREATE BLOCK
     */

    @Operation(summary = "TODO 생성")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "생성 완료"),
        @ApiResponse(responseCode = "403", description = "만료된 토큰"),
        @ApiResponse(responseCode = "404", description = "사용자 정보 일치하지 않음"),
    }
    )
    @PostMapping
    public ResponseEntity addTODO(@RequestBody TodoCreateReq request,
                                  @AuthenticationPrincipal SecurityUserDetails details) {

        Member member = memberService.findBySeq(details.getSeq());

        Todo todo = todoService.createTodo(member, request);

        return ResponseEntity.ok(todo);

    }


    /**
     * READ BLOCK
     */

    @Operation(summary = "가장 최근 TODO 조회")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "요청 성공"),
        @ApiResponse(responseCode = "403", description = "만료된 토큰"),
        @ApiResponse(responseCode = "404", description = "사용자 정보 일치하지 않음"),
    })
    @GetMapping("/lastest")
    public ResponseEntity findLastestTodo(@AuthenticationPrincipal SecurityUserDetails details) {

        Member member = memberService.findBySeq(details.getSeq());

        Todo todo = todoService.findLastestTodo(member);

        TodoFindRes result = todoMapper.toTodoFindRes(todo);

        return ResponseEntity.ok(result);

    }

    @Operation(summary = "TODO 목록 조회")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "요청 성공"),
        @ApiResponse(responseCode = "403", description = "만료된 토큰"),
        @ApiResponse(responseCode = "404", description = "사용자 정보 일치하지 않음"),
    })
    @GetMapping
    public ResponseEntity findTodos(
            int size, int page,
            @AuthenticationPrincipal SecurityUserDetails details) {

        Member member = memberService.findBySeq(details.getSeq());

        log.info("member :: {}", member);

        Page<Todo> todos = todoService.findTodos(member, PageRequest.of( page - 1 < 0 ? 0 : page -1, size));

        Page<TodoFindRes> result = todos.map(i -> todoMapper.toTodoFindRes(i));

        return ResponseEntity.ok(result);

    }

    /**
     * UPDATE BLOCK
     */
    @Operation(summary = "TODO 상태 변경")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "변경 완료"),
        @ApiResponse(responseCode = "403", description = "만료된 토큰"),
        @ApiResponse(responseCode = "404", description = "사용자 정보 일치하지 않음"),
        @ApiResponse(responseCode = "400", description = "진행중 상태에서만 대기 상태로 변경 가능"),
    })
    @PatchMapping("/{seq}")
    public ResponseEntity changeToTodo(
            @PathVariable Long seq,
            @RequestBody TodoChangeStatusReq request,
            @AuthenticationPrincipal SecurityUserDetails details) {

        Member member = memberService.findBySeq(details.getSeq());

        Todo afterTodo = todoService.changeStatus(member, seq, request.getStatus());
        TodoFindRes result = todoMapper.toTodoFindRes(afterTodo);

        return ResponseEntity.ok(result);

    }


    /**
     * DELETE BLOCK
     */

}
