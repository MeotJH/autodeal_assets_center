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
        public String opening_price;
        public String closing_price;
        public String min_price;
        public String max_price;
        public String units_traded;
        public String acc_trade_value;
        public String prev_closing_price;
        public String units_traded_24H;
        public String acc_trade_value_24H;
        public String fluctate_24H;
        public String fluctate_rate_24H;
    }

}
