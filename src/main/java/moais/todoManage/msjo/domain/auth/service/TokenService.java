package moais.todoManage.msjo.domain.auth.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import moais.todoManage.msjo.config.exception.BusinessException;
import moais.todoManage.msjo.domain.auth.dto.res.JwtIssueRes;
import moais.todoManage.msjo.domain.member.service.MemberService;
import moais.todoManage.msjo.entity.common.enums.UserType;
import moais.todoManage.msjo.entity.domain.Member;
import moais.todoManage.msjo.util.JwtUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

/**
 * packageName    : moais.todoManage.msjo.domain.auth.service
 * fileName       : TokenService
 * author         : ms.jo
 * date           : 2024. 6. 14.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 14.        ms.jo       최초 생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class TokenService {

    MemberService memberService;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;
//    StringRedisTemplate stringRedisTemplate;

    private final Long EXPIRE_TIME = 1440L;

    public JwtIssueRes issueMemberJwt(String id, String rawPassword) {

        Member member = memberService.findById(id);

        if (member == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND);
        }

        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED);
        }

        // TODO 1440분 = 1일로 설정(리프레시 토큰 추가시 30분으로 변경)
        JwtIssueRes result = new JwtIssueRes(jwtUtil.createJwt(UserType.Member, member.getSeq(), member.getNickname(), EXPIRE_TIME));

//        stringRedisTemplate.opsForValue().set(result.getAccessToken(), "TRUE", Duration.ofMinutes(EXPIRE_TIME));

        return result;
    }

}