package auto_deal.center.coin.domain;

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

}
