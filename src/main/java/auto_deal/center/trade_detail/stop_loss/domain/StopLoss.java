package auto_deal.center.trade_detail.stop_loss.domain;

import auto_deal.center.quant.domain.Quant;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TB_STOP_LOSS")
public class StopLoss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TB_STOP_LOSS_ID")
    private Long id;

    @Column
    @Getter
    private String coinTicker;

    @Column
    @Getter
    private Double dayOfMonthPrice;

    @Column
    @Getter
    private Double initPrice;

    @Column
    @Getter
    private Double stopLossPercent;

    @Column
    @Getter
    private Integer count;

    @Column
    @Getter
    private String resultCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="QUANT_ID")
    private Quant quant;

    @Column
    private LocalDateTime regdate;


    public void setQuant(Quant quant){
        this.quant = quant;

        if(!quant.getStopLosses().contains(this)){
            quant.getStopLosses().add(this);
        }
    }

    public void updateCount(Integer count){
        this.count = count;
    }

    public void updateStopLossPercent(Double stopLossPercent){
        this.stopLossPercent = stopLossPercent;
    }
}
