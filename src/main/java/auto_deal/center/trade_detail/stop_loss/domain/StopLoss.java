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
    private String coinTicker;

    @Column
    private Double initPrice;

    @Column
    private Double stopLossPercent;

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

    public void updateStopLossPercent(Double stopLossPercent){
        this.stopLossPercent = stopLossPercent;
    }
}
