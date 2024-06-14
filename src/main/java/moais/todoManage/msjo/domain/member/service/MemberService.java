package moais.todoManage.msjo.domain.member.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import moais.todoManage.msjo.config.exception.BusinessException;
import moais.todoManage.msjo.domain.member.dto.req.MemberCreateReq;
import moais.todoManage.msjo.domain.member.dto.req.MemberInactiveReq;
import moais.todoManage.msjo.domain.member.dto.req.MemberModifyNicknameReq;
import moais.todoManage.msjo.domain.member.repository.MemberRepository;
import moais.todoManage.msjo.entity.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * packageName    : moais.todoManage.msjo.domain.member.service
 * fileName       : MemberService
 * author         : ms.jo
 * date           : 2024. 6. 13.
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024. 6. 13.        ms.jo       최초 생성
 */
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    MemberRepository memberRepository;
    PasswordEncoder passwordEncoder;

    public Member createUser(MemberCreateReq request) {

        if(!isDuplicateId(request.getId()) || !isDuplicateNickname(request.getNickname())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST);
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));

        Member member = Member.of(request.getNickname(), request.getId(), request.getPassword());

        try {
            return memberRepository.save(member);
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            return Member.createEmptyObject();
        }

    }

    @Transactional(readOnly = true)
    public Member findBySeq(Long seq) {
        return memberRepository.findBySeq(seq);
    }

    @Transactional(readOnly = true)
    public Member findById(String id) {
        return memberRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Member findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    public boolean isDuplicateId(String id) {
        return !Objects.nonNull(findById(id));
    }

    public boolean isDuplicateNickname(String nickname) {
        return !Objects.nonNull(findByNickname(nickname));
    }

    @Transactional
    public void makeActive(Long memberSeq) {

        Member member = findBySeq(memberSeq);

        member.makeActive();

    }

    @Transactional
    public void makeInactive(Long memberSeq, MemberInactiveReq request) {

        Member member = findBySeq(memberSeq);

        member.makeInactive(request.getReason());

    }

    @Transactional
    public Member changeNickname(Long seq, MemberModifyNicknameReq request) {

        Member member = findBySeq(seq);
        member.changeNickname(request.getNickname());

        return member;

    }
}
