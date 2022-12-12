package auto_deal.center.quant.domain;

import auto_deal.center.trade_detail.domain.TradeDetail;
import auto_deal.center.user.domain.Users;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TB_QUANT")
public class Quant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="QUANT_ID")
    private Long id;

    @Column
    private String quantType;

    @OneToMany(mappedBy = "quant")
    @Builder.Default
    @Column
    private List<TradeDetail> tradeDetails = new ArrayList<>();

    @Column
    private LocalDateTime regdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @Setter
    @JoinColumn(name="USER_ID")
    private Users users;

    public void setUser(Users users){
        this.users = users;

        if(!users.getQuants().contains(this)){
            users.getQuants().add(this);
        }
    }

    public void addTradeDetail(TradeDetail detail){
        this.tradeDetails.add(detail);

        // 무한루프에 빠지지 않도록 체크
        if(detail.getQuant() != this){
            detail.setQuant(this);
        }
    }
}
