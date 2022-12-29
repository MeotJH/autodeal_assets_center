package auto_deal.center.coin.domain;

import auto_deal.center.api.coin.model.CoinApiRslt;
import auto_deal.center.coin.model.CoinInitModel;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="TB_COIN")
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticker;
    private String english;
    private String korea;
    private String opening_price;
    private String closing_price;
    private String min_price;
    private String max_price;
    private String units_traded;
    private String acc_trade_value;
    private String prev_closing_price;
    private String units_traded_24H;
    private String acc_trade_value_24H;
    private String fluctate_24H;
    private String fluctate_rate_24H;
    private Long three_month_avg_price;
    private Double now_price;

    public Coin updateCoinData(CoinApiRslt.Coin coin){
        this.opening_price = coin.getOpening_price();
        this.closing_price = coin.getClosing_price();
        this.min_price = coin.getMin_price();
        this.max_price = coin.getMax_price();
        this.units_traded = coin.getUnits_traded();
        this.acc_trade_value = coin.getAcc_trade_value();
        this.prev_closing_price = coin.getPrev_closing_price();
        this.units_traded_24H = coin.getUnits_traded_24H();
        this.acc_trade_value_24H = coin.getAcc_trade_value_24H();
        this.fluctate_24H = coin.getFluctate_24H();
        this.fluctate_rate_24H = coin.getFluctate_rate_24H();

        return this;
    }

    public Coin updateCoinName(CoinInitModel coin){
        this.korea = coin.getKorea();
        this.english = coin.getEnglish();
        return this;
    }

    public Coin update3MAvgPrice(Long price){
        this.three_month_avg_price = price;
        return this;
    }

    public Coin updateNowPrice(Double price){
        this.now_price = price;
        return this;
    }

}
