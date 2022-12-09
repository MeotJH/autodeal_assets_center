package auto_deal.center.api.coin.model;

import lombok.Getter;

import java.util.List;

@Getter
public class CoinOhlcvRslt {

    private String status;
    private List<List<String>> data;

}
