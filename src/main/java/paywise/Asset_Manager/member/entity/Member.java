package paywise.Asset_Manager.member.entity;

// --- [Import 영역] 외부 도구들을 빌려오는 곳입니다 ---
import jakarta.persistence.*; // JPA(DB 매핑 도구) 관련 기능을 가져옵니다.
import lombok.AccessLevel;    // 접근 제한자 설정을 위한 도구입니다.
import lombok.Getter;         // 코딩 없이 getter 메서드를 만들기 위해 가져옵니다.
import lombok.NoArgsConstructor; // 기본 생성자를 자동으로 만들기 위해 가져옵니다.

// --- [Annotation 영역] 이 클래스의 역할을 정해줍니다 ---
@Entity // "이 클래스는 DB의 'Member' 테이블과 1:1로 매칭되는 설계도야!"라고 선언합니다.
@Getter // 모든 필드(id, email 등)에 대해 getEmail(), getNickname() 같은 메서드를 자동으로 생성해줍니다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// JPA는 비어있는 기본 생성자가 꼭 필요합니다. 하지만 외부에서 'new Member()'로 함부로 만드는 걸 막기 위해
// 접근 권한을 'protected'로 제한해서 안전하게 보호합니다.
//=> 즉 내가 코드를 짜면서 실수로 생성자 하나 만들어서 생길 수 있는 문제를 막아줌.

public class Member {

    @Id // "이 필드가 이 테이블의 주인공(Primary Key)이야!"라고 알려줍니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // ID 값을 우리가 직접 넣지 않아도, DB가 알아서 1, 2, 3... 순서대로 숫자를 올려주게 설정합니다 (Auto Increment).
    private Long id;

    @Column(nullable = false, unique = true)
    // DB 테이블의 컬럼 설정을 합니다. (nullable=false: 비어있으면 안 됨, unique=true: 중복 이메일 금지)
    private String email;

    private String password;

    private String nickname;
}