package paywise.Asset_Manager.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import paywise.Asset_Manager.member.entity.Member;
import paywise.Asset_Manager.member.dto.MemberInfoResponseDto;
import paywise.Asset_Manager.member.dto.SignUpRequestDto;
import paywise.Asset_Manager.member.dto.UpdateMemberRequestDto;
import paywise.Asset_Manager.member.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정 (성능 최적화)
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional // 변경사항이 있을 때는 transactional을 추가하는 것이 좋다.
    public Long signUp(SignUpRequestDto requestDto) {
        // 1. 중복 이메일 검증 (금융권 시스템의 기본: 데이터 무결성)
        validateDuplicateEmail(requestDto.email());

        // 2. 비밀번호 암호화 (BCrypt 방식)
        String encodedPassword = passwordEncoder.encode(requestDto.password());

        // 3. Entity 생성 및 저장
        Member member = Member.builder()
                .email(requestDto.email())
                .password(encodedPassword)
                .nickname(requestDto.nickname())
                .totalBalance(0L) // 자산 관리의 시작점은 0원
                .build();//가져온 것을 토대로 여기서 만들어라!! build()의 역할

        Member savedMember = memberRepository.save(member);
        log.info("새로운 회원 가입 완료: ID={}, Email={}", savedMember.getId(), savedMember.getEmail());

        return savedMember.getId();
    }

    /**
     * 회원 정보 조회
     */
    public MemberInfoResponseDto getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다. ID: " + memberId));

        return new MemberInfoResponseDto(
                member.getEmail(),
                member.getNickname(),
                member.getTotalBalance()
        );
    }

    /**
     * 회원 정보 수정
     * JPA의 Dirty Checking(변경 감지)을 활용
     */
    @Transactional
    public void updateMember(Long memberId, UpdateMemberRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

        // 닉네임 변경
        if (requestDto.nickname() != null) {
            member.updateNickname(requestDto.nickname());
        }

        // 비밀번호 변경 (암호화 후 업데이트)
        if (requestDto.password() != null && !requestDto.password().isEmpty()) {
            member.updatePassword(passwordEncoder.encode(requestDto.password()));
        }

        log.info("회원 정보 수정 완료: ID={}", memberId);
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        memberRepository.deleteById(memberId);
        log.info("회원 탈퇴 처리 완료: ID={}", memberId);
    }

    /**
     * 이메일 중복 확인
     */
    public boolean checkEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }

    /**
     * 중복 검증 내부 로직
     */
    private void validateDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            log.warn("중복 가입 시도 감지: {}", email);
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }
    }
}