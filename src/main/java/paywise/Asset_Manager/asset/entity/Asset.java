package paywise.Asset_Manager.asset.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import paywise.Asset_Manager.member.entity.Member;
import paywise.Asset_Manager.stock.entity.Stock;
import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 여러 자산은 하나의 회원에 속함
    @JoinColumn(name = "member_id") //Asset 엔티티에 member_id라고 저장됨
    private Member member;
    /*일단 Member member위에 manytoone과 joincolumn이라는 어노테이션이 붙어있음
    그래서 member 테이블을 참조하는건 맞고 그 다음에 name=member_id라고 하는건
    내가 가져온 외래키를 저 이름으로 저장하겠다는 것이다. 그리고 referenceColumnName이라는 것을 통해
     @JoinColumn(name = "member_num", referencedColumnName = "member_number")
     이런식으로 쓸 경우 member 테이블에서 어떤 칼럼을 외래키로 가져올 지도 특정 할 수 있음
    */

    @ManyToOne(fetch = FetchType.LAZY) // 여러 자산 기록은 하나의 종목에 해당함
    @JoinColumn(name = "stock_id")
    private Stock stock;

    private Integer quantity; // 수량
    private BigDecimal avgPrice; // 평균단가
}