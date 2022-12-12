package auto_deal.center.trade_detail.domain;

import auto_deal.center.quant.domain.Quant;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TB_TRADE_DETAIL")
public class TradeDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TRADE_DETAIL_ID")
    private Long id;

    @Column
    private LocalDateTime alarmTime;

    @Column
    private String coinTicker;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name="QUANT_ID")
    private Quant quant;

    @Column
    private LocalDateTime regdate;

    public void setQuant(Quant quant){
        this.quant = quant;

        if(!quant.getTradeDetails().contains(this)){
            quant.getTradeDetails().add(this);
        }
    }


}
