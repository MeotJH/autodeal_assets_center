package auto_deal.center.trend_follow.domain;

import auto_deal.center.quant.domain.Quant;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TB_TREND_FOLLOW")
public class TrendFollow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TB_TREND_FOLLOW_ID")
    private Long id;

    @Column
    private String coinTicker;

    @Column
    private Boolean isBuy;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name="QUANT_ID")
    private Quant quant;

    @Column
    private LocalDateTime regdate;

    public void setQuant(Quant quant){
        this.quant = quant;

        if(!quant.getTrendFollows().contains(this)){
            quant.getTrendFollows().add(this);
        }
    }

}
