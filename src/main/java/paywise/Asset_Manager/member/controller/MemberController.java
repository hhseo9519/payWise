package paywise.Asset_Manager.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paywise.Asset_Manager.member.dto.MemberInfoResponseDto;
import paywise.Asset_Manager.member.dto.SignUpRequestDto;
import paywise.Asset_Manager.member.dto.UpdateMemberRequestDto;
import paywise.Asset_Manager.member.service.MemberService;

@Slf4j
@RestController
@RequestMapping("/api/members") // 버전을 제외한 깔끔한 공통 경로
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 API
     */
    @PostMapping("/signup")
    public ResponseEntity<Long> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        log.info("회원가입 프로세스 시작 - Email: {}", requestDto.email());
        Long memberId = memberService.signUp(requestDto);

        // 리소스 생성이 완료되었음을 의미하는 201 Created 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    /**
     * 내 정보 조회 API
     */
    @GetMapping("/me")
    public ResponseEntity<MemberInfoResponseDto> getMyInfo(@RequestParam Long memberId) {
        log.info("내 정보 조회 요청 - Member ID: {}", memberId);
        MemberInfoResponseDto response = memberService.getMyInfo(memberId);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 정보 수정 API
     */
    @PatchMapping("/me")
    public ResponseEntity<Void> updateMember(
            @RequestParam Long memberId,
            @Valid @RequestBody UpdateMemberRequestDto requestDto) {
        log.info("회원 정보 수정 시도 - Member ID: {}", memberId);
        memberService.updateMember(memberId, requestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 회원 탈퇴 API
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMember(@RequestParam Long memberId) {
        log.info("회원 탈퇴 처리 - Member ID: {}", memberId);
        memberService.deleteMember(memberId);

        // 삭제 성공 시 본문 데이터가 없음을 나타내는 204 No Content 반환
        return ResponseEntity.noContent().build();
    }

    /**
     * 이메일 중복 확인 API
     */
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        log.debug("이메일 중복 확인 - Email: {}", email);
        boolean isDuplicate = memberService.checkEmailDuplicate(email);
        return ResponseEntity.ok(isDuplicate);
    }
}