package paywise.Asset_Manager.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import paywise.Asset_Manager.auth.dto.JwtTokenResponseDto;
import paywise.Asset_Manager.auth.dto.LoginRequestDto;
import paywise.Asset_Manager.auth.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor// lombok = 생성자 주입
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        JwtTokenResponseDto response = authService.login(loginRequestDto);
        return ResponseEntity.ok(response);

    }

}
