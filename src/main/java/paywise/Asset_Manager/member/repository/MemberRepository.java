package paywise.Asset_Manager.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paywise.Asset_Manager.member.entity.Member;

import java.util.Optional;

// 1. class가 아니라 interface입니다!
// 2. implements가 아니라 extends입니다!
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //파라미터에 member와 long이 있는데 이건 제네릭이다.
    // 여기에 아무것도 안 적어도 기본 CRUD(저장, 조회, 삭제)는 이미 완성된 상태입니다..

    // 1. 이메일 중복 확인을 위한 메서드
    // SQL: SELECT count(*) > 0 FROM member WHERE email = ?
    boolean existsByEmail(String email);

    // 2. 이메일로 회원 정보를 가져오는 메서드 (로그인이나 상세 조회 시 필요)
    // SQL: SELECT * FROM member WHERE email = ?
    Optional<Member> findByEmail(String email);
    /* 여기서 optional 이라고 하는것은 null값도 나올수가 있다는거다
    원래는 반환형이 있을때 null이 나올수가 없었는데 optional<member>라는 말은
    null 이 나올수도 있고 반환이 뭔가 된다면 member타입으로 나올거야 라는 뜻이다.
    * */

    // 3. 닉네임 중복 확인 (나중에 필요할 수 있으니 미리 참고!)
    boolean existsByNickname(String nickname);

}