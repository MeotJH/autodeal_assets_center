package auto_deal.center.api.coin;

import auto_deal.center.api.coin.model.CoinApiRslt;
import auto_deal.center.api.coin.model.CoinOhlcvRslt;

import java.util.List;
import java.util.Map;

public interface CoinPrice {

    CoinApiRslt getPrices();

    CoinOhlcvRslt getOhlcv(String ticker);
}
