package paywise.Asset_Manager.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import paywise.Asset_Manager.auth.dto.JwtTokenResponseDto;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    // 생성자: yml에서 설정값을 가져와 '진짜 열쇠(Key)'를 깎습니다.
    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-expiration}") long accessTokenExpirationTime,
            @Value("${jwt.refresh-expiration}") long refreshTokenExpirationTime
    ) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    /**
     * [발급] 유저 정보를 토대로 Access/Refresh Token 생성
     */
    public JwtTokenResponseDto generateTokenDto(String email) {
        long now = (new Date()).getTime();

        // 1. Access Token 생성 (한 시간짜리 단기 통행증)
        String accessToken = Jwts.builder()
                .setSubject(email)                               // 유저 이메일
                .setExpiration(new Date(now + accessTokenExpirationTime)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256)        // 우리만의 열쇠로 서명
                .compact();
        //refresh 토큰은 access token을 새로 발급할 때 사용되는 토큰이다.
        // 2. Refresh Token 생성 (2주짜리 장기 재발급권)
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + refreshTokenExpirationTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtTokenResponseDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(now + accessTokenExpirationTime)
                .build();
    }

    /**
     * [검증] 토큰의 유효성(위조, 만료 등)을 확인합니다.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 비어있습니다.");
        }
        return false;
    }

    /**
     * [추출] 토큰 안에 숨겨진 유저 정보(Claims)를 꺼냅니다.
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰이라도 그 안에 든 정보는 꺼낼 수 있게 예외 처리를 합니다.
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String accessToken) {
        // 1. 토큰을 파싱하여 클레임(정보 덩어리)을 추출합니다.
        Claims claims = parseClaims(accessToken);

        // 2. 클레임에서 주체(Subject, 여기서는 Email)를 꺼내 UserDetails 객체를 생성합니다.
        // 현재 권한 정보(Role)를 토대로 로직을 짜지 않았으므로 빈 리스트(Collections.emptyList())를 넘깁니다.
        UserDetails principal = new User(claims.getSubject(), "", Collections.emptyList());

        // 3. 스프링 시큐리티의 인증 토큰 객체를 만들어 반환합니다.
        // principal(유저 정보), password(보통 보안상 비움), authorities(권한 목록)
        return new UsernamePasswordAuthenticationToken(principal, "", Collections.emptyList());
    }
}