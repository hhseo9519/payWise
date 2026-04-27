package paywise.Asset_Manager.auth.dto;

import lombok.Builder;

@Builder
public record JwtTokenResponseDto(
        String grantType,
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresIn
){}
