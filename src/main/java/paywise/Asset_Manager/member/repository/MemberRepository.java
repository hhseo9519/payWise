package paywise.Asset_Manager.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import paywise.Asset_Manager.member.entity.Member;

// 1. class가 아니라 interface입니다!
// 2. implements가 아니라 extends입니다!
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    //파라미터에 member와 long이 있는데 이건 제네릭이다.
    // 여기에 아무것도 안 적어도 기본 CRUD(저장, 조회, 삭제)는 이미 완성된 상태입니다..


}