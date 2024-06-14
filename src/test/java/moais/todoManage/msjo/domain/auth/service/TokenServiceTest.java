package moais.todoManage.msjo.domain.auth.service;

import moais.todoManage.msjo.domain.auth.dto.res.JwtIssueRes;
import moais.todoManage.msjo.domain.member.dto.req.MemberCreateReq;
import moais.todoManage.msjo.domain.member.service.MemberService;
import moais.todoManage.msjo.entity.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName    : moais.todoManage.msjo.domain.auth.service
 * fileName       : TokenServiceTest
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
class TokenServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    TokenService tokenService;

    @Test
    public void 토큰은_올바르게_발행돼야_한다() {

        Member member = memberService.createUser(new MemberCreateReq("msjo", "nick", "qwe123!@#"));

        JwtIssueRes jwtIssueRes = tokenService.issueMemberJwt(member.getId(), "qwe123!@#");

        System.out.println("jwtIssueRes = " + jwtIssueRes.getAccessToken());

        assertNotNull(jwtIssueRes.getAccessToken());

    }

}