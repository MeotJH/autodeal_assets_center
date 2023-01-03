package auto_deal.center.api.coin;

import auto_deal.center.api.coin.model.CoinApiRslt;
import auto_deal.center.api.coin.model.CoinOhlcvRslt;
import org.json.JSONObject;

public interface CoinPrice {

    CoinApiRslt getPriceData();

    CoinOhlcvRslt getOhlcv(String ticker);

    JSONObject getNowPrices();

    JSONObject getNowPrice(String ticker);
}
