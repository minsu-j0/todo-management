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
import moais.todoManage.msjo.entity.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


/**
 * packageName    : moais.todoManage.msjo.controller
 * fileName       : MemberController
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
@RequestMapping("/members")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
@Tag(name = "사용자 API")
public class MemberController {

    MemberService memberService;
    TokenService tokenService;

    /**
     * CREATE BLOCK
     */

    @Operation(summary = "사용자 생성", security = {})
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "생성 완료"),
        @ApiResponse(responseCode = "400", description = "ID or Nickname 중복"),
    }
    )
    @PostMapping("/join")
    public ResponseEntity addMember(@RequestBody MemberCreateReq request) {

        String rawPassword = request.getPassword();

        Member member = memberService.createUser(request);

        JwtIssueRes result = tokenService.issueMemberJwt(member.getId(), rawPassword);

        return ResponseEntity.ok(result);

    }


    /**
     * READ BLOCK
     */

    @Operation(summary = "ID 중복 체크")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "true : 사용 가능한 ID\nfalse : 이미 사용 중인 ID"),
    })
    @GetMapping("/check/duplicates/id/{id}")
    public ResponseEntity checkDuplicateId(@PathVariable("id") String id) {

        boolean result = memberService.isDuplicateId(id);

        return ResponseEntity.ok(result);

    }

    @Operation(summary = "Nickname 중복 체크")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "true : 사용 가능한 nickname\nfalse : 이미 사용 중인 nickname"),
    })
    @GetMapping("/check/duplicates/nickname/{nickname}")
    public ResponseEntity checkDuplicateNickname(@PathVariable("nickname") String nickname) {

    boolean result = memberService.isDuplicateNickname(nickname);

    return ResponseEntity.ok(result);

    }


    /**
     * UPDATE BLOCK
     */
    @Operation(summary = "사용자 닉네임 변경")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "변경 완료"),
        @ApiResponse(responseCode = "403", description = "만료된 토큰"),
        @ApiResponse(responseCode = "404", description = "사용자 정보 일치하지 않음"),
    })
    @PatchMapping("/nickname")
    public ResponseEntity changeNickname(
            @RequestBody MemberModifyNicknameReq request,
            @AuthenticationPrincipal SecurityUserDetails member) {

        log.info("request :: {}", request);
        log.info("member :: {}", member.getNickname());

        Member result = memberService.changeNickname(member.getSeq(), request);

        return ResponseEntity.ok(result.getNickname());

    }

    @Operation(summary = "사용자 계정 비활성화 요청")
    @ApiResponses(value ={
        @ApiResponse(responseCode = "200", description = "비활성화 완료"),
        @ApiResponse(responseCode = "403", description = "만료된 토큰"),
        @ApiResponse(responseCode = "404", description = "사용자 정보 일치하지 않음"),
    })
    @PatchMapping("/inactive")
    public ResponseEntity inactiveMember(
            @AuthenticationPrincipal SecurityUserDetails member,
            @RequestBody MemberInactiveReq request) {

        memberService.makeInactive(member.getSeq(), request);

        return ResponseEntity.ok().build();

    }

    /**
     * DELETE BLOCK
     */

    /**
     * TODO Delete 요청의 경우 정책에 따라 존재할 수도, 존재하지 않을 수 있다.
     */

}
