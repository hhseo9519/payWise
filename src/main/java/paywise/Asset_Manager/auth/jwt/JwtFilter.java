package paywise.Asset_Manager.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 요청 헤더에서 토큰을 꺼내옵니다.
        String jwt = resolveToken(request);

        // 2. 토큰이 있고, 유효성 검사를 통과한다면
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            // 토큰에서 인증 정보(유저 정보)를 가져와서
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            // 스프링 시큐리티의 '보관함(SecurityContext)'에 저장합니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 3. 다음 필터로 요청을 넘깁니다. (이게 없으면 다음 단계로 못 가요!)
        filterChain.doFilter(request, response);
    }

    /**
     * [추출] Request Header에서 토큰 정보를 꺼내오기 위한 메서드
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7); // "Bearer " 이후의 실제 토큰 문자열만 반환
        }
        return null;
    }
}