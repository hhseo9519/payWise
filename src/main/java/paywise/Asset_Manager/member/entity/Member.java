package paywise.Asset_Manager.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false) // 비밀번호는 비어있으면 안 됩니다.
    private String password;

    @Column(nullable = false) // 닉네임도 필수값으로 설정하는 것이 안전합니다.
    private String nickname;

    // --- [자산 관련 필드 추가] ---
    @Column(nullable = false)
    private Long totalBalance;
    // 1. 왜 Long인가요? 금융 데이터는 액수가 커질 수 있고, 소수점 계산보다 원단위 정수 처리가 정확하기 때문입니다.
    // 2. 초기값은 서비스 단(Service)에서 0L로 넣어주거나, 아래처럼 기본값을 설정할 수 있습니다.

    // --- [비즈니스 로직 메서드] ---

    /**
     * 닉네임 수정
     */
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 비밀번호 수정
     */
    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    /**
     * 잔액 업데이트 (입출금 발생 시 호출)
     * 금융권에서는 데이터 정합성이 중요하므로 이런 핵심 로직은 엔티티 내부에서 처리하는 것을 선호합니다.
     */
    public void updateBalance(Long amount) {
        this.totalBalance = amount;
    }
}