package paywise.Asset_Manager.member.dto;

public record SignUpRequestDto(
        String email,
        String password,
        String nickname
) {}