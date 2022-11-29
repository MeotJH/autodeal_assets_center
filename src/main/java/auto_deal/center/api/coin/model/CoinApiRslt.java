package auto_deal.center.api.coin.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@NoArgsConstructor
@Slf4j
@ToString
public class CoinApiRslt {

    private String status;

    private Map<String, Coin> data;
    @Setter
    private String date;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coin {
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

}
