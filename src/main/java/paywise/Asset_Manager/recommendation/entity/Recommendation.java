package paywise.Asset_Manager.recommendation.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import paywise.Asset_Manager.member.entity.Member;
import paywise.Asset_Manager.stock.entity.Stock;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 추천받는 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id") // pk인 칼럼을 가져올 때만 referecedColumnName을 쓰지 않아도 된다.
    private Stock stock;   // 추천 종목

    @Column(length = 1000)
    private String reason; // 추천 사유 (AI가 분석한 내용 등)

    private LocalDateTime recommendedAt; // 추천 시점
}