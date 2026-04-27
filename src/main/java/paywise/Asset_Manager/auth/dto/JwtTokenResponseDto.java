package paywise.Asset_Manager.auth.dto;

public record JwtTokenResponseDto(
        String grantType,
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresIn
){}
