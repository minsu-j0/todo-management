package moais.todoManage.msjo.domain.member.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import moais.todoManage.msjo.config.security.SecurityUserInfo;
import moais.todoManage.msjo.entity.domain.Member;
import org.springframework.context.annotation.Configuration;

/**
 * packageName    : moais.todoManage.msjo.domain.member.mapper
 * fileName       : MemberMapper
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
public class MemberMapper {

    public SecurityUserInfo toSecurityUserInfo(Member member) {
        return new SecurityUserInfo(member.getId(), member.getPassword(), member.getSeq(), member.getNickname());
    }

}
