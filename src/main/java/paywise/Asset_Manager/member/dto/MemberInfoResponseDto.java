package paywise.Asset_Manager.member.dto;

public record MemberInfoResponseDto(
        String email,
        String nickname,
        Long totalBalance // 자산 관리 서비스이므로 총 잔액 포함
) {}
