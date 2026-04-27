package paywise.Asset_Manager.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import paywise.Asset_Manager.auth.jwt.JwtFilter;
import paywise.Asset_Manager.auth.jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // JwtTokenProvider 주입을 위해 추가
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // 2. [출입 통제 매뉴얼] 여기서 chain을 설정합니다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // REST API는 보통 끕니다.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 쓸 거니까 세션은 안 써요!
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // 로그인, 회원가입은 프리패스
                        .anyRequest().authenticated()); // 나머지는 검문소 통과해야 함

        // 3. [보안 요원 배치] 나중에 여기에 JWT 필터를 끼워 넣을 거예요!
        // UsernamePasswordAuthenticationFilter(기본 로그인 필터)가 실행되기 전에
        // 우리가 만든 JwtFilter를 먼저 실행하라는 설정입니다.
        http.addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}