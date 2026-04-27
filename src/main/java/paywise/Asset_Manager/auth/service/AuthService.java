package paywise.Asset_Manager.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import paywise.Asset_Manager.auth.dto.JwtTokenResponseDto;
import paywise.Asset_Manager.auth.dto.LoginRequestDto;
import paywise.Asset_Manager.auth.jwt.JwtTokenProvider;
import paywise.Asset_Manager.member.entity.Member;
import paywise.Asset_Manager.member.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly=true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenResponseDto login(LoginRequestDto loginRequestDto) {

        Member member = memberRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new RuntimeException("로그인 정보가 올바르지 않습니다."));
        /*optional로 필터링이 안 된다면 member가 들어갔다는 말이다.
        근데 어떻게 member에 아이디랑 비번만 줬는데 다른 nickname 같은 필드들도 채워졌지?
        이건 당연히 findByEmail에서 select문으로 가져와서 그거 그대로 다 넣어주니까 채워지지..
        */
        if(!passwordEncoder.matches(loginRequestDto.password(), member.getPassword())) {
            throw new RuntimeException("로그인 정보가 올바르지 않습니다.");
        }
        return jwtTokenProvider.generateTokenDto(member.getEmail());


    }

}
